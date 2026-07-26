# DayDrops

Plugin para Spigot/Paper que permite configurar un **ítem exclusivo** que reciben los jugadores al matar a otro jugador, **ignorando por completo el keepInventory**.

Cuando un jugador muere:
- No dropea su inventario ni su cabeza (se elimina cualquier drop natural).
- El asesino recibe automáticamente el ítem configurado en el menú.
- Esto ocurre siempre, tenga o no la víctima `keepInventory` activado.

## Requisitos

- Servidor Spigot o Paper 1.20.x
- Java 17+

## Instalación

1. Descargá `DayDrops.jar` (compilado desde Actions o localmente con `mvn clean package`).
2. Colocalo en la carpeta `plugins/` de tu servidor.
3. Reiniciá el servidor o hacé `/reload`.
4. Se generará automáticamente `plugins/DayDrops/config.yml`.

## Comandos

| Comando | Descripción | Permiso |
|---|---|---|
| `/daydrops` | Abre el menú de configuración (54 slots) | `daydrops.use` |
| `/daydrops help` | Muestra la lista de comandos | - |
| `/daydrops reload` | Recarga la configuración desde el archivo | `daydrops.admin` |
| `/daydrops enable` | Activa el plugin | `daydrops.admin` |
| `/daydrops disable` | Desactiva el plugin | `daydrops.admin` |

Alias: `/dd`

Todos los subcomandos tienen autocompletado con **tab**.

## Permisos

| Permiso | Descripción | Default |
|---|---|---|
| `daydrops.admin` | Permite reload, enable, disable y colocar/quitar el ítem en el menú | `op` |
| `daydrops.use` | Permite abrir el menú de configuración | `op` |

## Cómo configurar el ítem

1. Ejecutá `/daydrops`.
2. Se abre un menú de 54 slots con los bordes decorados con paneles grises.
3. En el centro vas a ver una **estrella del nether** con el texto *"Coloca el item que dropearán los jugadores"*.
4. Con un ítem en el cursor, hacé click en ese slot central: queda guardado como el ítem que van a recibir todos los jugadores al matar a alguien.
5. Para quitarlo, hacé click de nuevo sobre el ítem ya configurado (sin nada en el cursor).

El cambio se guarda automáticamente en `config.yml`, no hace falta `/daydrops reload`.

## Configuración (`config.yml`)

```yaml
enabled: true

messages:
  no-permission: "&cNo tienes permiso para usar este comando."
  only-players: "&cSolo los jugadores pueden usar este comando."
  reloaded: "&aLa configuración de DayDrops se recargó correctamente."
  enabled: "&aDayDrops ha sido activado."
  disabled: "&cDayDrops ha sido desactivado."
  already-enabled: "&eDayDrops ya estaba activado."
  already-disabled: "&eDayDrops ya estaba desactivado."
  item-set: "&aEl item de dropeo se actualizó correctamente."
  item-removed: "&eSe quitó el item de dropeo configurado."
  menu-title: "&8DayDrops - Configuración"
  unknown-command: "&cComando desconocido. Usa /daydrops help"
```

Todos los mensajes admiten códigos de color con `&`.

## Compilación

El repo incluye un workflow de GitHub Actions (`.github/workflows/build.yml`) que compila el `.jar` automáticamente en cada push a `main`. El resultado queda disponible como artifact descargable en la pestaña **Actions**.

Para compilar en local:

```bash
mvn clean package
```

El `.jar` queda en `target/DayDrops.jar`.

## Autor

Bughatti
