import json
from asyncio import new_event_loop, set_event_loop
from os import environ
from types import SimpleNamespace
from datetime import datetime, timedelta
import pytest_asyncio

import pytest
from alembic.command import upgrade
from httpx import AsyncClient
from sqlalchemy import create_engine
from sqlalchemy.orm import Session, sessionmaker
from sqlalchemy.sql import exists
from sqlalchemy_utils import create_database, database_exists, drop_database

from quick_wallet.database.models import Card
from tests.utils import make_alembic_config

from quick_wallet.__main__ import get_app
from quick_wallet.config import get_settings
from .prepared_data import shops, users, cards
from quick_wallet.database.models.storage import Shop
from quick_wallet.database.models.account import User
from quick_wallet.database.models.access_control import VerificationCode


@pytest.fixture(scope="session")
def event_loop():
    loop = new_event_loop()
    set_event_loop(loop)

    yield loop
    loop.close()


@pytest.fixture(scope="session")
def db_name():
    environ["POSTGRES_DB"] = f"{get_settings().POSTGRES_DB}_test"


@pytest.fixture(scope="session")
def db_engine(db_name):
    engine = create_engine(get_settings().sync_database_uri)
    yield engine
    engine.dispose()


@pytest.fixture(scope="session")
def db_for_tests(db_engine):
    if not database_exists(db_engine.url):
        create_database(db_engine.url)
    yield
    drop_database(db_engine.url)


@pytest.fixture(scope="session")
def migrate(db_for_tests):
    alembic_config = make_alembic_config(
        SimpleNamespace(
            config="quick_wallet/database/", name="alembic", pg_url=get_settings().sync_database_uri, raiseerr=False,
            x=None
        )
    )
    upgrade(alembic_config, "head")
    yield


@pytest.fixture(scope="session", autouse=True)
def prepare_shops(migrate):
    engine = create_engine(get_settings().sync_database_uri)
    session = sessionmaker(engine, expire_on_commit=False, class_=Session)()
    for shop_data in shops:
        new_shop = Shop(**shop_data)
        session.add(new_shop)
    session.commit()
    yield


@pytest.fixture
def prepare_users():
    engine = create_engine(get_settings().sync_database_uri)
    session = sessionmaker(engine, expire_on_commit=False, class_=Session)()
    for user_data in users:
        new_user = User(**user_data)
        session.add(new_user)
        new_ver_code = VerificationCode(receiver=new_user.phone, code="0000",
                                        date_expire=datetime.now() + timedelta(days=2), date_created=datetime.now() - timedelta(days=1))
        session.add(new_ver_code)
    session.commit()
    yield
    session.query(User).delete()
    session.query(VerificationCode).delete()
    session.commit()


@pytest.fixture
def prepare_cards():
    engine = create_engine(get_settings().sync_database_uri)
    session = sessionmaker(engine, expire_on_commit=False, class_=Session)()
    for card_data in cards:
        new_card = Card(**card_data)
        session.add(new_card)
    session.commit()
    yield
    session.query(Card).delete()
    session.commit()


@pytest_asyncio.fixture
async def client() -> AsyncClient:
    app = get_app()
    yield AsyncClient(app=app, base_url="http://test")
