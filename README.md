# Links
- [Official Discord](https://discord.gg/ujY2mV9)<br/>

- [Immersive Technology on CurseForge](https://www.curseforge.com/minecraft/mc-mods/immersive-technology)
- [Immersive Technology on Modrinth](https://modrinth.com/mod/mct-immersive-technology)

# MCT Immersive Technology
Energy Technology addon for Immersive Engineering, forked from Immersive Tech.<br/>

# Reporting issues
When you are reporting bugs, please attach the crash report, mod and forge version.<br/>

# Help translate the mod
Feel free to translate the mod and put it in a pull request.<br/>

# Local development
Immersive Convergence now lives in this repository as a git submodule under `external/ImmersiveConvergence`.<br/>

Run `git submodule update --init --recursive` after cloning so the dependency is available locally.<br/>

Build and run this project from the top-level repository as usual. The root Gradle build will invoke the IC submodule first and then consume its local `dev` jar from `external/ImmersiveConvergence/build/libs`.<br/>

When working on both mods together, commit Immersive Convergence changes inside the submodule repo and then commit the updated submodule pointer in this repo.<br/>

# About Modpack and License
Immersive Technology is licensed under the GNU GENERAL PUBLIC LICENSE Version 3. You may use it in modpacks, reviews or any other form as long as you abide by the terms.<br/>
