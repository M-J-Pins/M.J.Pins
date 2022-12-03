from fastapi import APIRouter, Body, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.schemas.auth import AuthResponse, PhoneAuthRequest
from quick_wallet.services.auth import AuthActionResult, AuthManager

api_router = APIRouter(prefix="/auth")


@api_router.post(
    "/auth",
    response_model=AuthResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_404_NOT_FOUND: {
            "description": "No user with such phone or no such code",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def auth_request_admin(
    request: PhoneAuthRequest = Body(
        ...,
        example={
            "phone": "+79507990996",
            "code": "1234",
        },
    ),
    db: AsyncSession = Depends(get_session),
):
    """
    Ручка принимает почту и код подтверждения
    Возвращает jwt авторизации
    """
    result, token = await AuthManager().phone_auth(db, request.code, request.phone)
    if result == AuthActionResult.AUTH_OK:
        return {"token": token}
    raise HTTPException(
        status_code=result.value,
    )
