from uuid import UUID

from pydantic import AnyHttpUrl, BaseModel
from pydantic.fields import List, Optional

from quick_wallet.database.models.storage import (
    CardColorEnum,
    CardTypeEnum,
    ShopCategoryEnum,
)


class CardResponse(BaseModel):
    id: UUID
    type: CardTypeEnum
    barcode_data: str
    shop_name: str
    image_url: Optional[AnyHttpUrl]
    color: Optional[CardColorEnum]
    category: ShopCategoryEnum

    class Config:
        orm_mode = True


class CardListResponse(BaseModel):
    cards: List[CardResponse] = []

    class Config:
        orm_mode = True
