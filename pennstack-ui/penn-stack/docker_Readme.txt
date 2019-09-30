
docker ps
docker login --username=mdprakash
docker images
docker tag b930619421d9 mdprakash/penn-vue-docker-container:v1
docker push mdprakash/penn-vue-docker-container:v1
docker stop f40104de51da


docker run -d -p 2012:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=spring_app_db -e MYSQL_USER=app_user -e MYSQL_PASSWORD=test123 mysql:latest

docker build -t mdprakash/pennstack-frontendvueapp .
docker run -it -d -p 8080:80 --name pennstackfrontend mdprakash/pennstack-frontendvueapp
docker push mdprakash/pennstack-frontendvueapp


docker tag mysql:latest mdprakash/pennstack-mysqlbackend
docker push mdprakash/pennstack-mysqlbackend
docker build -t mdprakash/pennstack-backendservice .
bca43e00442a