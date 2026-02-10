---
name: exploring-mc-source
description: Explores and analyzes Minecraft source code (jar files in .gradle/loom-cache) for Fabric mod development. Investigates class hierarchies, game systems (entity, world, item, block, network), and integration points for modding. Triggers on requests like "Minecraftのソースを調べて", "エンティティシステムの仕組みを教えて", "Minecraftの実装を確認して", or any Minecraft source code analysis task.
---

# Minecraft Source Code Exploration

## Source Extraction

Extract sources from the loom-cache jar. If the sources jar does not exist, run `./gradlew genSourcesWithVineFlower` first.

```bash
mkdir -p temp/minecraft-sources
unzip -q ".gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-common-*/*/minecraft-common-*-sources.jar" -d temp/minecraft-sources
```

Verify extraction succeeded:
```bash
ls temp/minecraft-sources/net/minecraft/ | head
# Expected: block  client  entity  item  network  server  world  ...
```

Extracted sources: `temp/minecraft-sources/net/minecraft/` (~4000 Java files). Main packages: `entity`, `world`, `item`, `block`, `server`, `client`, `network`.

## Analysis Workflow

1. **Extract** sources (above)
2. **Verify** extraction output contains expected packages
3. **Explore** using strategies below
4. **Clean up** when done: `rm -rf temp/minecraft-sources`

## Exploration Strategies

Choose strategy based on the goal. Use the Explore agent for broad analysis; use direct Grep/Glob for targeted queries.

| Goal | Strategy | Example |
|------|----------|---------|
| Understand a package | Package Structure | `entity/` の全体像把握 |
| Map inheritance | Class Hierarchy | `Entity` の継承ツリー調査 |
| Find connections | Cross-Package Deps | `world` と `entity` の依存関係 |
| Locate a feature | Feature Search | ダメージ計算の実装箇所特定 |

### Package Structure Overview
Glob `temp/minecraft-sources/net/minecraft/[package]/**/*.java` to list files. Read key classes to understand the hierarchy.

### Class Hierarchy Analysis
Grep for `class.*extends` and `implements` within target packages to map inheritance.

### Cross-Package Dependencies
Grep for import statements between packages to map relationships.

### Feature Search
Glob for `*[feature]*` file names and grep for feature keywords across the codebase.

## Detailed Analysis Guide

For advanced techniques (design patterns, performance analysis, practical examples per system): [references/analysis-guide.md](references/analysis-guide.md).

## Best Practices

- Use the Explore agent for large-scale analysis across many files
- Focus analysis scope on relevant packages for the task
- Combine multiple strategies (hierarchy + dependency + pattern) for comprehensive understanding
- Always clean up extracted files after analysis