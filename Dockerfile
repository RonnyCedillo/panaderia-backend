# 1. Usamos una imagen que ya tiene Maven y Java 17 instalados
FROM maven:3.8.5-openjdk-17

# 2. Creamos una carpeta de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiamos todos tus archivos del proyecto al contenedor
COPY . .

# 4. Ejecutamos el comando para compilar (crear el .jar)
RUN mvn clean package -DskipTests

# 5. Le decimos al contenedor que exponga el puerto 7071 (el de tu Javalin)
EXPOSE 7071

# 6. El comando para arrancar la aplicación
# OJO: Asegúrate de que el nombre del archivo .jar coincida con tu pom.xml
# Si seguiste mis pasos, debería ser este:
CMD ["java", "-jar", "target/panaderia-proyect-1.0-SNAPSHOT.jar"]