from math import sqrt
from typing import ByteString, List
from uuid import UUID

import pytesseract
from colorthief import ColorThief
from fastapi import File
from PIL import Image
from pysimilar import compare
from sqlalchemy.ext.asyncio import AsyncSession

from quick_wallet.database.models.analysis import ShopCardColor, ShopCardText
from quick_wallet.schemas.misc import ColorRGB


class ShopSimilarityManager:
    def __init__(self, text_sim_rate: int = 1, color_sim_rate: int = 1):
        self.text_sim_rate = text_sim_rate
        self.color_sim_rate = color_sim_rate

    @staticmethod
    def get_text_from_image(image: Image) -> str:
        text = pytesseract.image_to_string(image, lang="rus+eng")
        print(text)
        return text

    @staticmethod
    def count_image_color(image: File) -> ColorRGB:
        color_thief = ColorThief(image)
        dominant_color = color_thief.get_color(quality=1)
        return ColorRGB(
            r=dominant_color[0],
            g=dominant_color[1],
            b=dominant_color[2],
        )

    async def find_the_most_similar_shop_v1(
        self,
        db: AsyncSession,
        shop_ids: list[UUID],
        card_text: str,
        card_color: ColorRGB,
        minimal_similarity: int = 0,
    ) -> UUID:
        """
        This algorithm will analyse the text extracted from the photo and avg color to choose the most similar shop

        :param db: Async session
        :param shop_ids: List of shops id to search throw
        :param card_text: Text extracted from the front side card photo
        :param card_color: Average color of the photo
        :param minimal_similarity: Minimal similarity rate to choose shop
        :return: ID of the most similar shop
        """
        best_id = None
        max_sim = 0
        for shop_id in shop_ids:
            max_cur_shop_text_sim = 0
            max_cur_shop_color_sim = 0

            db_texts: List[ShopCardText] = await ShopCardText.get_all(
                db, shop_id=shop_id
            )
            db_texts = [] if db_texts is None else db_texts
            for cur_text in db_texts:
                print(f"!!!!! {cur_text} !!!\n\n")
                max_cur_shop_text_sim = max(
                    max_cur_shop_text_sim, self.count_text_sim(card_text, cur_text.text)
                )

            db_colors: List[ShopCardColor] = await ShopCardColor.get_all(
                db, shop_id=shop_id
            )
            db_colors = [] if db_colors is None else db_colors

            db_colors_rgb: List[ColorRGB] = [
                self.hex_to_rgb(color.color) for color in db_colors
            ]
            for cur_color in db_colors_rgb:
                max_cur_shop_color_sim = max(
                    max_cur_shop_color_sim,
                    self.count_color_sim(card_color, cur_color),
                )

            cur_sim = (
                self.text_sim_rate * max_cur_shop_text_sim
                + self.color_sim_rate * max_cur_shop_color_sim
            )
            if cur_sim > max_sim and cur_sim > minimal_similarity:
                max_sim = cur_sim
                best_id = shop_id
        return best_id

    @staticmethod
    def count_text_sim(text1: str, text2: str) -> float:
        return compare(text1, text2)

    @staticmethod
    def count_color_sim(color1: ColorRGB, color2: ColorRGB) -> float:
        diff = sqrt(
            (color1.r - color2.r) ** 2
            + (color1.g - color2.g) ** 2
            + (color1.b - color1.b) ** 2
        )
        max_diff = 255**2 * 3
        return (max_diff - diff) / max_diff

    @staticmethod
    def hex_to_rgb(hex: str) -> ColorRGB:
        rgb = ColorRGB()
        rgb.r = int(hex[0:2], 16)
        rgb.g = int(hex[2:4], 16)
        rgb.b = int(hex[4:6], 16)
        return rgb
