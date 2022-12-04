import json
from typing import Dict, List

import requests
from geopy.distance import great_circle
from pysimilar import compare

from quick_wallet.config import get_settings

from .classes import Point, PointParser


class MapSearchManager:
    @staticmethod
    def distance_to_nearest_shop(
        point: Point, shop_search_strings: str, max_radius: int
    ) -> float:
        """
        :param point: Search box center
        :param shop_search_strings: A string containing comma-separated variants of the shop name on the map
        :param max_radius: Half side of search box
        :return: Minimal distance to the shop with the most name similarity rate
        """
        min_distance = max_radius
        max_similarity = 0
        for shop_string in shop_search_strings.split(","):
            response = requests.get(
                f"https://search-maps.yandex.ru/v1/"
                f"?apikey={get_settings().YANDEX_API_KEY}"
                f"&text={shop_string}"
                f"&lang=ru_RU"
                f"&type=biz"
                f"&ll={point.long_lat_str()}"
                f"&spn={PointParser.meters2point(point, max_radius).long_lat_str()}"
                f"&rspn=1"
            )

            for shop_data in json.loads(response.content.decode()).get("features", []):
                cur_distance = max_radius
                cur_similarity = compare(shop_string, shop_data.get("name", ""))
                point_list = shop_data.get("geometry", {}).get("coordinates", None)
                if point_list is not None:
                    cur_distance = great_circle(
                        point.lat_long_pair(), Point(*point_list).lat_long_pair()
                    ).meters

                if cur_similarity > max_similarity:
                    if cur_distance < min_distance:
                        max_similarity = cur_similarity
                        min_distance = cur_distance

        return min_distance
