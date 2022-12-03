from typing import List

from fastapi import APIRouter, Body, Depends, HTTPException, Query
from pydantic import BaseModel
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from quick_wallet.database.connection import get_session
from quick_wallet.database.models import User
from quick_wallet.schemas.auth import AuthResponse
from quick_wallet.services.misc import JWTManager


class PhoneRequest(BaseModel):
    phone: str


api_router = APIRouter()


@api_router.post(
    "/hard_test",
    response_model=AuthResponse,
    status_code=status.HTTP_200_OK,
    responses={
        status.HTTP_422_UNPROCESSABLE_ENTITY: {
            "description": "Invalid data (validation error)",
        },
        status.HTTP_500_INTERNAL_SERVER_ERROR: {
            "description": "Oops.. Server was broken by your request",
        },
    },
)
async def hard_test(
    request: PhoneRequest = Body(
        ...,
        example={
            "phone": "79507990996",
        },
    ),
    db: AsyncSession = Depends(get_session),
):
    user = await User.get(db, phone=request.phone)

    return AuthResponse(token=str(await JWTManager().generate_jwt(user.id)))
