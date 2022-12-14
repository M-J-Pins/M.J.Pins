APPLICATION_NAME = quick_wallet
CODE = $(APPLICATION_NAME)


run:  ##@Application Run application
	poetry run python3 -m $(APPLICATION_NAME)

db:  ##@Database Create database with docker-compose
	docker-compose -f docker-compose.yml up -d --remove-orphans

lint:  ##@Code Check code with pylint
	poetry run python3 -m pylint $(CODE)

format:  ##@Code Reformat code with isort and black
	poetry run python3 -m isort $(CODE)
	poetry run python3 -m black $(CODE)

open_db:  ##@Database Open database inside docker-image
	docker exec -it $(POSTGRES_DB) psql -d $(POSTGRES_DB) -U $(POSTGRES_USER)

revision:  ##@Database Create new revision file automatically with prefix
	cd $(APPLICATION_NAME)/database && alembic revision --autogenerate

migrate:  ##@Database Do all migrations in database
	cd $(APPLICATION_NAME)/database && alembic upgrade head

deploy:
	gunicorn -k uvicorn.workers.UvicornWorker -b "0.0.0.0:8000" quick_wallet.__main__:app

test:
	poetry run python3 -m pytest --verbosity=2 --showlocals --log-level=DEBUG

test-cov:
	poetry run python3 -m pytest --verbosity=2 --showlocals --log-level=DEBUG --cov=quick_wallet

