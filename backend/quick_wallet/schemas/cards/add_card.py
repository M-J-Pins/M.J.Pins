from uuid import UUID

from quick_wallet.schemas.base import AuthorizedRequest
from quick_wallet.database.models.storage import ShopCategoryEnum


class AddCardBaseRequest(AuthorizedRequest):
    barcode_data: str
    note: str


class AddUnknownCardRequest(AddCardBaseRequest):
    shop_name: str
    category: ShopCategoryEnum

    class Config:
        orm_mode = True


class AddStandardCardRequest(AddCardBaseRequest):
    shop_id: UUID

    class Config:
        orm_mode = True



