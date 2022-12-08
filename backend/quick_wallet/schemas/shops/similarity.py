from uuid import UUID

from .base import ShopResponse


class MostSimilarShopResponse(ShopResponse):
    add_card_action_id: UUID
