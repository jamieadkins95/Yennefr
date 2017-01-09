# -*- coding: utf-8 -*-
"""
Created on Sat Nov 12 07:50:06 2016

@author: Chris
Amended by Jamie Adkins (jamieadkins95)
"""

import requests, os, bs4, time
import json
import logging as log
import os.path
import string
import re

#!python3

def cardInfoFromGwentDb():
    # All rarities won't have 4 pages, so lots of information is process twice. Doesn't matter as it will just overwrite with the same info.
    for page in range(1, 5):
      for rarity in range(1, 5):
        queries = {'filter-display': '1', 'page': page, 'filter-rarity': rarity}
        url = 'http://www.gwentdb.com/cards'
        print(url)
        res = requests.post(url, params=queries)
        res.encoding = 'UTF-8'
        res.raise_for_status()

        soup = bs4.BeautifulSoup(res.text, "lxml")

        for cardRow in soup.find_all('tr'):
            # Check we are dealing with a row that contains card data.
            if cardRow.get('class') != ["card-row"]:
                continue

            cardData = {
                'name': "",
                'type': "",
                'faction': "",
                'rows': {},
                'rarity': "",
                'loyalty': [],
                'strength': "",
                'image': "",
                'cardid': "",
                'info': "",
            }

            imageUrl = cardRow.get('data-card-image-url')
            cardData['image'] = imageUrl

            # This page doesn't provide rarity data, so we abuse the filters instead.
            cardData['rarity'] = getRarityString(rarity)

            # Get everything after last slash.
            cardId = imageUrl.split('/')[-1]
            # Cut 'icon.png' off the end.
            cardId = cardId.split('icon.png')[0]
            cardData['cardid'] = cardId

            for details in cardRow.find_all('td'):
                 if 'col-row' in details.get('class'):
                     for row in details:
                         cardData['rows'][row.get_text()] = True

                 if 'col-power' in details.get('class'):
                    cardData['strength'] = details.contents[0]

                 if 'col-type' in details.get('class'):
                    cardData['type'] = details.contents[0]

                 if 'col-faction' in details.get('class'):
                    cardData['faction'] = details.contents[0]

                 if 'col-loyalty' in details.get('class'):
                    for loyaltySpan in details.find_all('span'):
                        cardData['loyalty'].append(loyaltySpan.get('title'))

                 if 'col-abilities' in details.get('class'):
                    cardData['info'] = removeHtml(details.span.get('title'))

                 if 'col-name' in details.get('class'):
                     cardData['name'] = details.a.contents[0]

            print(cardData)
            cards[cardData['cardid']] = cardData

def getRarityString(rarityId):
    if rarityId == 1:
        return "Common"
    elif rarityId == 2:
        return "Rare"
    elif rarityId == 3:
        return "Epic"
    else: # rarityId == '4'
        return  "Legendary"

def removeHtml(raw_html):
  cleanr = re.compile('<.*?>')
  cleantext = re.sub(cleanr, '', raw_html)
  return cleantext

def saveCardsAsJson(filename, cards):
    log.debug("saveCardsAsJson() saving %s cards to %s", len(cards), filename)
    filepath = os.path.join('./' + filename)
    print(filepath)
    with open(filepath, "w", newline="\n") as f:
        json.dump(cards, f, sort_keys=True, indent=2, separators=(',', ': '))

cards = {}
cardInfoFromGwentDb()
saveCardsAsJson('latest.json', cards)
