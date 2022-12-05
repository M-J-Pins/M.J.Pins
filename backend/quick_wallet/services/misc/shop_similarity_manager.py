from math import sqrt
from uuid import UUID

from pysimilar import compare
from singleton_decorator import singleton
from sqlalchemy.ext.asyncio import AsyncSession

from quick_wallet.database.models.analysis import ShopCardColor, ShopCardText
from quick_wallet.schemas.misc import ColorRGB


class ShopSimilarityManager:
    def __init__(self, text_sim_rate: int = 1, color_sim_rate: int = 1):
        self.text_sim_rate = text_sim_rate
        self.color_sim_rate = color_sim_rate

    async def find_the_most_suitable_shop(
        self,
        db: AsyncSession,
        shop_ids: list[UUID],
        card_text: str,
        card_color: str,
        minimal_similarity: int = 0,
    ) -> UUID:
        best_id = None
        max_sim = 0
        for shop_id in shop_ids:
            max_cur_shop_text_sim = 0
            max_cur_shop_color_sim = 0

            db_texts = await ShopCardText.get(db, shop_id=shop_id)
            db_texts = [] if db_texts is None else db_texts
            for cur_text in db_texts:
                max_cur_shop_text_sim = max(
                    max_cur_shop_text_sim, self.count_text_sim(card_text, cur_text)
                )

            db_colors = await ShopCardColor.get(db, shop_id=shop_id)
            db_colors = [] if db_colors is None else db_colors

            db_colors = [self.hex_to_rgb(color) for color in db_colors]
            for cur_color in db_colors:
                max_cur_shop_color_sim = max(
                    max_cur_shop_color_sim,
                    self.count_color_sim(self.hex_to_rgb(card_color), cur_color),
                )

            cur_sim = (
                self.text_sim_rate * max_cur_shop_text_sim
                + self.color_sim_rate * max_cur_shop_color_sim
            )
            if cur_sim > max_sim and cur_sim > minimal_similarity:
                max_sim = cur_sim
                best_id = shop_id
        return best_id

    def count_text_sim(self, text1: str, text2: str) -> float:
        return compare(text1, text2)

    def count_color_sim(self, color1: ColorRGB, color2: ColorRGB) -> float:
        diff = sqrt(
            (color1.r - color2.r) ** 2
            + (color1.g - color2.g) ** 2
            + (color1.b - color1.b) ** 2
        )
        max_diff = 255**2 * 3
        return (max_diff - diff) / max_diff

    def hex_to_rgb(self, hex: str) -> ColorRGB:
        rgb = ColorRGB()
        rgb.r = int(hex[0:2], 16)
        rgb.g = int(hex[2:4], 16)
        rgb.b = int(hex[4:6], 16)
        return rgb
