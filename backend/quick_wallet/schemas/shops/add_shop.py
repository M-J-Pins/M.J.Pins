from pydantic import AnyHttpUrl, BaseModel

from quick_wallet.database.models.storage import ShopCategoryEnum
from quick_wallet.schemas.base import AdminRequest


class AddShopRequest(AdminRequest):
    name: str
    map_search_string: str
    category: ShopCategoryEnum

    class Config:
        orm_mode = True
