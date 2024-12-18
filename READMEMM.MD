

detener los contenedores
docker-compose down

crear base de datos desde cero
docker volume rm mareo-envios-master_postgres_data


en el caso de que querramos eliminar las imagenes podemos usar esto: 
docker image rm mareo-envios-master-app1 mareo-envios-master-app2 mareo-envios-master-app3 

construir imagenes:
docker-compose build

levantar contenedores:
docker-compose up

estos dos ultimos comandos se pueden tirar juntos:

docker-compose up --build
