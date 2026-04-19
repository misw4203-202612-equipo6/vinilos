# Vinilos Mobile App

Aplicación Android desarrollada con Kotlin y Jetpack Compose.

## Requisitos

- Android Studio instalado.
- JDK configurado. Android Studio normalmente incluye uno en su instalación.
- Android SDK disponible desde Android Studio.
- Conexión a internet para sincronizar dependencias de Gradle la primera vez.

## Abrir el proyecto en Android Studio

1. Abre Android Studio.
2. Selecciona **Open**.
3. Busca y abre la carpeta del proyecto:

   ```text
   mobile_app
   ```

4. Espera a que Android Studio sincronice Gradle.
5. Si Android Studio muestra el botón **Sync Now**, haz clic en él.

## Crear un Resizable Device

1. En Android Studio abre **Device Manager**.
2. Haz clic en **Create Device**.
3. En la lista de dispositivos selecciona **Resizable**.
4. Haz clic en **Next**.
5. Selecciona una imagen de sistema recomendada, preferiblemente una versión reciente de Android.
6. Si la imagen no está descargada, haz clic en **Download** y espera a que termine.
7. Haz clic en **Next**.
8. Asigna un nombre al emulador, por ejemplo:

   ```text
   Vinilos Resizable Device
   ```

9. Finaliza con **Finish**.

## Ejecutar la aplicación

1. En la barra superior de Android Studio, selecciona el emulador **Vinilos Resizable Device** o el nombre que hayas elegido.
2. Verifica que el módulo seleccionado sea **app**.
3. Haz clic en **Run**.
4. Espera a que Gradle compile e instale la aplicación en el emulador.

Cuando la app arranque deberías ver la pantalla principal de **Vinilos**, con accesos a:

- Albums
- Artistas
- Coleccionistas

## Probar tamaños de pantalla

El dispositivo **Resizable** permite probar distintos tamaños sin crear varios emuladores.

Con la app abierta en el emulador:

1. Usa los controles del emulador para cambiar el tamaño de la ventana.
2. Prueba la app en formatos pequeños y grandes.
3. Revisa que las listas, botones, encabezado y buscador se mantengan visibles y ordenados.