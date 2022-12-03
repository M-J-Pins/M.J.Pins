from pydantic import BaseModel, AnyHttpUrl

from quick_wallet.schemas.base import AdminRequest
from quick_wallet.database.models.storage import ShopCategoryEnum


class AddShopRequest(AdminRequest):
    name: str
    map_search_string: str
    category: ShopCategoryEnum

    class Config:
        orm_mode = True
