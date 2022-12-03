from fastapi import APIRouter
from starlette import status

from quick_wallet.database.models.storage import ShopCategoryEnum
from quick_wallet.schemas.shops import CategoryListResponse

api_router = APIRouter(prefix="/shops")


@api_router.post(
    "/categories",
    response_model=CategoryListResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def get_categories():
    """
    Ручка возвращает список категорий магазинов
    """
    response: CategoryListResponse = CategoryListResponse(
        categories=[cat.value for cat in ShopCategoryEnum]
    )
    return response
