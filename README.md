# 🤖 Bot de Telegram para Reservas de Gimnasio 🏋️‍♀️

Bot desarrollado en **Java** que permite a los usuarios ver profesores disponibles, elegir horarios y **reservar clases** de un gimnasio directamente desde Telegram.  
Las reservas se guardan en un archivo **JSON**, que actúa como base de datos local.

---

## 🚀 Funcionalidades principales
- Comando `/start`: envía un mensaje de bienvenida con opciones.  
- Comando `/profes`: muestra una lista de profesores con **botones interactivos**.  
- Al seleccionar un profesor:
  - Muestra los **días y horarios** disponibles.  
  - Permite elegir un horario y guarda la reserva en el JSON.  
- Botón **"Volver"** para regresar al listado de profesores.  

---

## 🧠 Tecnologías usadas
- **Java**
- **Telegram Bot API (Java)** — librería [TelegramBots](https://github.com/rubenlagus/TelegramBots)
- **JSON (org.json o Gson)** para manejo de datos

---

