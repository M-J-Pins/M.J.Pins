from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models.storage import CardTypeEnum
from quick_wallet.schemas.cards import AddUnknownCardRequest, AddCardResponse
from quick_wallet.services.cards import CardManager
from quick_wallet.services.access import FunctionCallResult

api_router = APIRouter(prefix="/card")


@api_router.post(
    "/add_unknown",
    response_model=AddCardResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_401_UNAUTHORIZED: {
            "description": "Invalid or unknown token",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def auth_request_admin(
    request: AddUnknownCardRequest = Body(
        ...,
        example={
            "token": "your jwt token",
            "barcode_data": "10398475822",
            "note": "My new card",
            "category": "SPORT",
            "shop_name": "Alpha",
        },
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает информацию о новой unknown карте
    Добавляет карту в базу и отдает объект карты
    """
    result, new_card = await CardManager.add_card(
        db,
        request.token,
        request.barcode_data,
        request.note,
        CardTypeEnum.UNKNOWN,
        shop_name=request.shop_name,
        category=request.category,
    )
    if result == FunctionCallResult.SUCCESS:
        return AddCardResponse.from_orm(new_card)
    raise HTTPException(
        status_code=result.value,
    )