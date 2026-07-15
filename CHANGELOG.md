# Changelog / 更新日志

All notable changes to the MyLog IntelliJ plugin will be documented in this file.
本文件记录 MyLog IntelliJ 插件的所有重要变更。

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

格式基于 [Keep a Changelog](https://keepachangelog.com/zh_CN/1.1.0/)，
本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [Unreleased] / 未发布

## [1.1.0] - 2026-07-15

### Added / 新增
- Kotlin (`.kt`/`.kts`) and Python (`.py`) language support
  新增 Kotlin 和 Python 语言支持
- Kotlin template with `${class}`, `${line}`, `${var}` variables
  Kotlin 模板，支持 `${class}`、`${line}`、`${var}` 变量
- Python template with `${file}`, `${line}`, `${var}` variables
  Python 模板，支持 `${file}`、`${line}`、`${var}` 变量

### Changed / 变更
- Language detection now uses file extension matching instead of binary Java check
  语言检测从二选一改为基于文件后缀的多路匹配

## [1.0.1] - 2026-07-09

### Fixed / 修复
- Correct insert line calculation for log statement placement
  修复日志语句插入行的计算逻辑

## [1.0.0] - 2026-07-08

### Added / 新增
- `MyLogAction` with Option+L shortcut for console.log generation
  新增 `MyLogAction`，支持 Option+L 快捷键生成 console.log
- Java/JS template support with settings configurable
  新增 Java/JS 模板支持，模板内容可通过设置自定义

### Fixed / 修复
- Set `pluginUntilBuild` to `252.*` (IntelliJ 2025.2)
  设置 `pluginUntilBuild` 为 `252.*`（适配 IntelliJ 2025.2）
- Marketplace validation errors
  修复插件市场验证错误
- Add `instrumentationTools()` dependency to fix `buildPlugin` task
  添加 `instrumentationTools()` 依赖以修复 `buildPlugin` 任务
- Add command name for undo support and handle last-line newline edge case
  添加命令名称以支持撤销操作，并处理文件末尾换行的边界情况
- Remove unused `pluginName` property
  移除未使用的 `pluginName` 属性

### Changed / 变更
- Gradle build configuration for MyLog plugin
  初始化 Gradle 构建配置

---

[Unreleased]: https://github.com/my/MyLog/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/my/MyLog/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/my/MyLog/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/my/MyLog/releases/tag/v1.0.0