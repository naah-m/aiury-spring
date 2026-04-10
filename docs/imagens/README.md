# Artefatos Visuais da Entrega

Esta pasta concentra as versoes visuais usadas na avaliacao da disciplina:
- `der.png`
- `diagrama-classes.png`
- `arquitetura.png`

## Fontes oficiais dos diagramas
- `docs/diagramas/der.mmd`
- `docs/diagramas/diagrama-classes.puml`
- `docs/diagramas/diagrama-classes.mmd`
- `docs/diagramas/arquitetura.mmd`

## Como regenerar os PNGs (Mermaid CLI)
Com Node/NPM disponiveis:

```powershell
npx -y @mermaid-js/mermaid-cli@11.4.2 -i docs/diagramas/der.mmd -o docs/imagens/der.png
npx -y @mermaid-js/mermaid-cli@11.4.2 -i docs/diagramas/diagrama-classes.mmd -o docs/imagens/diagrama-classes.png
npx -y @mermaid-js/mermaid-cli@11.4.2 -i docs/diagramas/arquitetura.mmd -o docs/imagens/arquitetura.png
```

Observacao:
- o arquivo `.puml` do diagrama de classes e mantido como fonte versionavel adicional;
- a exportacao usada nesta entrega foi feita a partir do equivalente Mermaid para manter automacao simples no ambiente.
