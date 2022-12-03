import random
from typing import Any
from uuid import UUID

from sqlalchemy.ext.asyncio import AsyncSession

from quick_wallet.services.access import authorized
from quick_wallet.database.models.storage import Card, CardColorEnum, CardTypeEnum, Shop, ShopCategoryEnum


class CardManager:

    @staticmethod
    @authorized
    async def add_card(
        db: AsyncSession,
        owner_id: Any,
        barcode_data: str,
        note: str,
        type: CardTypeEnum,
        shop_id: UUID = "",
        shop_name: str = "",
        category: ShopCategoryEnum = ShopCategoryEnum.ANY,
    ) -> Card:
        new_card: Card = None
        if type == CardTypeEnum.UNKNOWN:
            color = random.choice(list(CardColorEnum))
            new_card = await Card.create(
                db,
                type=type,
                owner_id=owner_id,
                barcode_data=barcode_data,
                note=note,
                shop_name=shop_name,
                color=color,
                category=category,
            )
        if type == CardTypeEnum.STANDART:
            db_shop: Shop = await Shop.get(db, shop_id=shop_id)
            new_card = await Card.create(
                db,
                type=type,
                owner_id=owner_id,
                barcode_data=barcode_data,
                note=note,
                shop_id=db_shop.id,
                image_url=db_shop.card_image_url,
                shop_name=db_shop.name,
                map_search_string=db_shop.map_search_string,
                category=db_shop.category,
            )
        return new_card