from __future__ import annotations

import math


class Point:
    def __init__(self, long, lat):
        self.long = long
        self.lat = lat

    def lat_long_pair(self) -> (float, float):
        return self.lat, self.long

    def long_lat_pair(self) -> (float, float):
        return self.long, self.lat

    def lat_long_str(self) -> str:
        return f"{self.lat},{self.long}"

    def long_lat_str(self) -> str:
        return f"{self.long},{self.lat}"


class PointParser:
    @staticmethod
    def meters2point(point: Point, radius_m: int) -> Point:
        lat = math.radians(point.lat)
        lon = math.radians(point.long)

        earth_radius = 6371000
        parallel_radius = earth_radius * math.cos(lat)

        rad2deg = math.degrees
        delta_lat = rad2deg(radius_m / earth_radius)
        delta_long = rad2deg(radius_m / parallel_radius)

        return Point(delta_long, delta_lat)

    @staticmethod
    def long_lat_str2point(long_lat_str: str) -> Point:
        long = long_lat_str.split(",")[0]
        lat = long_lat_str.split(",")[1]
        return Point(long, lat)
