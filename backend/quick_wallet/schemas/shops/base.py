from uuid import UUID

from pydantic import AnyHttpUrl, BaseModel
from pydantic.fields import List

from quick_wallet.database.models.storage import ShopCategoryEnum


class ShopResponse(BaseModel):
    id: UUID
    name: str
    map_search_string: str
    icon_url: AnyHttpUrl
    category: ShopCategoryEnum
    card_image_url: AnyHttpUrl

    class Config:
        orm_mode = True


class ShopListResponse(BaseModel):
    shops: List[ShopResponse] = []

    class Config:
        orm_mode = True


class CategoryListResponse(BaseModel):
    categories: List[ShopCategoryEnum] = []
