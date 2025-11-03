<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&height=160&section=header&text=Klux%20Event%20System&fontSize=60&fontAlignY=35&animation=fadeIn"/>
</p>

<p align="center">
  <strong>⚡ The fastest, reflection-free, helper event bus for Fabric / Minecraft</strong><br>
  Inspired by <a href="https://github.com/MeteorDevelopment/orbit">MeteorClient's Orbit</a> – but lighter, cleaner and copy-left friendly.
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#quick-start">Quick Start</a> •
  <a href="discord.gg/klux">Discord</a> •
  <a href="#license">License</a>
</p>

---

## 🌟 Features

| Feature | Description |
|---------|-------------|
| **Lambda Speed** | Listeners are compiled to `Consumer<Event>` at start-up – no reflection after bootstrap |
| **Priority & Inheritance** | Events are delivered in priority order and walk the class hierarchy |
| **Cancellable** | Built-in `Cancellable` interface for stop-propagation semantics |
| **Thread-Safe** | Subscribe / unsubscribe from any thread without locks |
| **Zero Dependencies** | Only Fabric-Loader required – no Kotlin, no extra libs |
| **Helper Mode** | Drop-in side-bus for GUI, combat, render or utility mods |

---

## 🚀 Quick Start
a
1. Add to your `build.gradle`:

```groovy
modImplementation include('tech.klux:(jar name)')
