# Click Quest Backend API

This service exposes endpoints for recording and retrieving game scores.

Base URL:
- Local: http://localhost:3001
- Docs: /docs (redirects to Swagger UI)
- OpenAPI JSON: /api-docs

## Endpoints

- POST /api/scores
  - Body:
    {
      "playerName": "Alice",
      "points": 123
    }
  - Returns: 201 Created with created score

- GET /api/scores/top
  - Returns: top 10 scores as array (highest first)

## CORS

The allowed origin is configured via:
- app.cors.allowed-origin property (application.properties)
- or FRONTEND_ORIGIN env var, default http://localhost:3000

Example:
FRONTEND_ORIGIN=https://your-frontend.app

## Notes

- H2 in-memory DB used by default; data resets on restart.
- See src/main/resources/application.properties for details.
