from uuid import UUID

import orjson
from pydantic import BaseModel, AnyHttpUrl
from pydantic.fields import Optional

from quick_wallet.schemas.base import AuthorizedRequest
from quick_wallet.database.models.storage import ShopCategoryEnum, Card, CardTypeEnum, CardColorEnum


class AddCardBaseRequest(AuthorizedRequest):
    barcode_data: str
    note: str


class AddUnknownCardRequest(AddCardBaseRequest):
    shop_name: str
    category: ShopCategoryEnum

    class Config:
        orm_mode = True


class AddStandartCardRequest(AddCardBaseRequest):
    shop_id: UUID

    class Config:
        orm_mode = True


class AddCardResponse(BaseModel):
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
