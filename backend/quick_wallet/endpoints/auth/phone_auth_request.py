from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.schemas.auth import AuthRequestResponse, PhoneAuthRequestRequest
from quick_wallet.services.auth import AuthActionResult, AuthManager

api_router = APIRouter(prefix="/auth")


@api_router.post(
    "/auth_request",
    response_model=AuthRequestResponse,
    status_code=status.HTTP_202_ACCEPTED,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_423_LOCKED: {
            "description": "Cooldown is not expired",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def auth_request_admin(
    request: PhoneAuthRequestRequest = Body(
        ...,
        example={
            "phone": "+79507990996",
        },
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает запрос на отправку кода авторизации на номер телефона пользователя
    """

    result = await AuthManager().phone_auth_request(db, request.phone)
    if result == AuthActionResult.CODE_SENT_SUCCESSFULLY:
        return {"description": "Code was sent successfully"}
    raise HTTPException(
        status_code=result.value,
    )
