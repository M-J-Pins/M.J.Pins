from uuid import UUID

from pydantic.fields import Optional

from quick_wallet.database.models.storage import ShopCategoryEnum
from quick_wallet.schemas.base import AuthorizedRequest


class AddCardBaseRequest(AuthorizedRequest):
    barcode_data: str
    add_card_action_id: Optional[UUID]


class AddUnknownCardRequest(AddCardBaseRequest):
    shop_name: str
    category: ShopCategoryEnum

    class Config:
        orm_mode = True


class AddStandardCardRequest(AddCardBaseRequest):
    shop_id: UUID

    class Config:
        orm_mode = True
