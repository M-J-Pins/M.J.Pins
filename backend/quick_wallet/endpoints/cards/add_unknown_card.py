from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models.storage import CardTypeEnum
from quick_wallet.schemas.cards import AddUnknownCardRequest, CardResponse
from quick_wallet.services.access import FunctionCallResult
from quick_wallet.services.cards import CardManager
from quick_wallet.database.models.analysis import AddCardAction

api_router = APIRouter(prefix="/cards")


@api_router.post(
    "/unknown",
    response_model=CardResponse,
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
async def add_unknown_card(
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
        CardTypeEnum.UNKNOWN,
        shop_name=request.shop_name,
        category=request.category,
    )

    if request.add_card_action_id is not None:
        add_card_action: AddCardAction = await AddCardAction.get(db, id=request.add_card_action_id)
        if add_card_action is not None:
            add_card_action.user_choice_shop_name = request.shop_name
            await db.commit()

    if result == FunctionCallResult.SUCCESS:
        return CardResponse.from_orm(new_card)
    raise HTTPException(
        status_code=result.value,
    )
