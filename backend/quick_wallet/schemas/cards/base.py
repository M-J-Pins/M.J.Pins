from uuid import UUID

from pydantic import BaseModel, AnyHttpUrl
from pydantic.fields import Optional, List

from quick_wallet.database.models.storage import ShopCategoryEnum, CardTypeEnum, CardColorEnum


class CardResponse(BaseModel):
    id: UUID
    type: CardTypeEnum
    barcode_data: str
    note: str
    shop_name: str
    image_url: Optional[AnyHttpUrl]
    color: Optional[CardColorEnum]
    category: ShopCategoryEnum

    class Config:
        orm_mode = True


class CardListResponse(BaseModel):
    cards: List[CardResponse]

    class Config:
        orm_mode = True
