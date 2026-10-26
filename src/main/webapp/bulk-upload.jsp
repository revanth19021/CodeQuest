/* =========================================================
   CODEQUEST RESPONSIVE THEME
   Shared styles + responsive bulk-upload page
   Breakpoints: 1024px, 768px, 480px, 360px
   ========================================================= */

:root {
    color-scheme: dark;
    --cq-bg: #070b18;
    --cq-surface: #10162b;
    --cq-surface-raised: #151d36;
    --cq-surface-deep: #0b1124;
    --cq-border: #25345c;
    --cq-border-soft: #344365;
    --cq-purple: #8b35ff;
    --cq-purple-light: #a78bfa;
    --cq-cyan: #00d9ff;
    --cq-text: #f4f6ff;
    --cq-muted: #a1accb;
    --cq-success: #22c55e;
    --cq-danger: #fb7185;
    --cq-radius: 16px;
    --cq-shadow: 0 18px 55px rgba(0, 0, 0, .28);
}

*,
*::before,
*::after {
    box-sizing: border-box;
}

html {
    min-height: 100%;
    scroll-behavior: smooth;
    -webkit-text-size-adjust: 100%;
}

body {
    margin: 0;
    min-height: 100vh;
    background:
        radial-gradient(ellipse at 15% 0%, rgba(139, 53, 255, .12), transparent 38%),
        radial-gradient(ellipse at 100% 90%, rgba(0, 217, 255, .07), transparent 32%),
        var(--cq-bg);
    color: var(--cq-text);
    font-family: Inter, "Segoe UI", Arial, Helvetica, sans-serif;
    line-height: 1.6;
    overflow-wrap: break-word;
}

img,
svg,
video,
canvas,
iframe {
    max-width: 100%;
}

img,
video {
    height: auto;
}

a {
    color: var(--cq-cyan);
    text-decoration: none;
    transition: color .18s ease, opacity .18s ease;
}

a:hover {
    color: #9cefff;
}

a:focus-visible,
button:focus-visible,
input:focus-visible,
select:focus-visible,
textarea:focus-visible {
    outline: 3px solid rgba(0, 217, 255, .75);
    outline-offset: 3px;
}

button,
input,
select,
textarea {
    font: inherit;
}

button {
    cursor: pointer;
}

/* Shared layout helpers: safe defaults for other CodeQuest pages */
.container,
.main-container,
.page-content {
    width: 100%;
    max-width: 1200px;
    margin-inline: auto;
    padding: 24px;
}

.card,
.dashboard-card,
.mission-card,
.form-card,
.result-card {
    min-width: 0;
    padding: 24px;
    background: var(--cq-surface);
    border: 1px solid var(--cq-border);
    border-radius: var(--cq-radius);
    box-shadow: 0 8px 26px rgba(0, 0, 0, .12);
}

.card-grid,
.dashboard-grid,
.missions-grid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 20px;
}

nav,
.navbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 16px;
    padding: 16px 24px;
    background: rgba(16, 22, 43, .96);
    border-bottom: 1px solid var(--cq-border);
}

.nav-links {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 16px;
}

.nav-links a,
.navbar a {
    color: var(--cq-text);
}

form {
    min-width: 0;
}

label {
    display: block;
    margin-bottom: 8px;
    color: var(--cq-text);
    font-size: .92rem;
    font-weight: 650;
}

input:not([type="checkbox"]):not([type="radio"]),
select,
textarea {
    width: 100%;
    max-width: 100%;
    min-width: 0;
    padding: 12px 14px;
    margin-bottom: 16px;
    border: 1px solid var(--cq-border);
    border-radius: 9px;
    background: var(--cq-surface-deep);
    color: var(--cq-text);
}

textarea {
    min-height: 120px;
    resize: vertical;
}

button,
.btn,
.button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    max-width: 100%;
    padding: 12px 18px;
    border: 0;
    border-radius: 9px;
    background: linear-gradient(110deg, var(--cq-purple), #6436d9);
    color: #fff;
    font-weight: 700;
    text-align: center;
    transition: transform .18s ease, filter .18s ease;
}

button:hover,
.btn:hover,
.button:hover {
    filter: brightness(1.1);
    transform: translateY(-1px);
}

button:disabled {
    cursor: not-allowed;
    opacity: .6;
    transform: none;
}

.table-container {
    width: 100%;
    overflow-x: auto;
    border: 1px solid var(--cq-border);
    border-radius: 10px;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    padding: 12px;
    border-bottom: 1px solid var(--cq-border);
    text-align: left;
    vertical-align: top;
}

th {
    background: var(--cq-surface-raised);
    color: var(--cq-text);
}

td {
    color: var(--cq-muted);
}

.success-message,
.error-message,
.alert {
    padding: 12px 16px;
    margin: 16px 0;
    border-radius: 9px;
    overflow-wrap: anywhere;
}

.success-message {
    border: 1px solid var(--cq-success);
    background: rgba(34, 197, 94, .10);
    color: #bbf7d0;
}

.error-message {
    border: 1px solid var(--cq-danger);
    background: rgba(251, 113, 133, .10);
    color: #fecdd3;
}

/* Shared result styles */
.result-card {
    width: min(100% - 32px, 650px);
    margin: 40px auto;
    text-align: center;
}

.result-icon {
    font-size: 46px;
}

.result-correct {
    color: var(--cq-success);
}

.result-incorrect {
    color: var(--cq-danger);
}

.xp-earned {
    color: var(--cq-cyan);
    font-size: 1.5rem;
    font-weight: 800;
}

.result-actions {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 12px;
    margin-top: 24px;
}

/* =========================================================
   BULK UPLOAD PAGE
   ========================================================= */

.bulk-upload-page {
    min-height: 100vh;
}

.bulk-page {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    width: 100%;
    min-height: 100vh;
    padding: clamp(24px, 5vw, 64px) 20px;
}

.bulk-card {
    width: 100%;
    max-width: 780px;
    min-width: 0;
    padding: clamp(22px, 4vw, 42px);
    background: linear-gradient(160deg, rgba(20, 28, 54, .98), rgba(12, 18, 37, .98));
    border: 1px solid rgba(101, 119, 171, .35);
    border-radius: 22px;
    box-shadow: var(--cq-shadow);
}

.bulk-header {
    margin: 0 0 30px;
    text-align: center;
}

.bulk-icon {
    display: grid;
    place-items: center;
    width: 66px;
    height: 66px;
    margin: 0 auto 16px;
    border: 1px solid rgba(167, 139, 250, .7);
    border-radius: 19px;
    background: linear-gradient(135deg, rgba(139, 53, 255, .24), rgba(0, 217, 255, .08));
    box-shadow: 0 8px 26px rgba(139, 53, 255, .12);
    font-size: 30px;
}

.eyebrow {
    margin: 0 0 8px;
    color: var(--cq-cyan);
    font-size: .72rem;
    font-weight: 800;
    letter-spacing: .19em;
}

.bulk-header h1 {
    margin: 0 0 12px;
    font-size: clamp(1.65rem, 3.5vw, 2.15rem);
    line-height: 1.2;
    letter-spacing: -.035em;
}

.bulk-header > p:last-child,
.bulk-description {
    max-width: 590px;
    margin: 0 auto;
    color: var(--cq-muted);
    font-size: .96rem;
    line-height: 1.75;
}

.bulk-section {
    min-width: 0;
    padding: clamp(16px, 3vw, 24px);
    margin-bottom: 20px;
    border: 1px solid rgba(64, 82, 128, .55);
    border-radius: 15px;
    background: rgba(7, 11, 24, .55);
}

.bulk-section h2 {
    margin: 0 0 9px;
    font-size: 1.12rem;
    letter-spacing: -.01em;
}

.bulk-section .bulk-description {
    margin: 0;
    font-size: .9rem;
}

.type-list {
    display: flex;
    flex-wrap: wrap;
    gap: 9px;
    margin-top: 18px;
}

.type-badge {
    display: inline-flex;
    align-items: center;
    min-height: 34px;
    padding: 7px 12px;
    border: 1px solid var(--cq-border-soft);
    border-radius: 999px;
    background: var(--cq-surface-raised);
    color: #e3e9ff;
    font-size: .8rem;
    font-weight: 650;
}

.type-badge.mcq {
    border-color: rgba(167, 139, 250, .7);
    color: #d8c6ff;
}

.type-badge.fill {
    border-color: rgba(0, 217, 255, .55);
    color: #a5f3fc;
}

.type-badge.code {
    border-color: rgba(34, 197, 94, .55);
    color: #bbf7d0;
}

.bulk-form {
    margin-top: 20px;
}

.bulk-form label {
    margin-bottom: 10px;
}

.bulk-form input[type="file"] {
    display: block;
    width: 100%;
    min-width: 0;
    padding: 12px;
    margin: 0;
    border: 1px dashed #53648d;
    border-radius: 10px;
    background: rgba(16, 22, 43, .8);
    color: var(--cq-text);
    font-size: .88rem;
}

.bulk-form input[type="file"]::file-selector-button {
    max-width: 100%;
    margin: 0 12px 0 0;
    padding: 9px 12px;
    border: 0;
    border-radius: 7px;
    background: #25345c;
    color: #fff;
    font-weight: 650;
    cursor: pointer;
}

.bulk-submit {
    width: 100%;
    min-height: 48px;
    margin-top: 18px;
    padding: 13px 18px;
    border-radius: 10px;
    font-size: .94rem;
}

.bulk-message {
    padding: 14px 16px;
    margin: 0 0 20px;
    border-radius: 10px;
    font-size: .9rem;
    line-height: 1.65;
    overflow-wrap: anywhere;
}

.bulk-message.success {
    border: 1px solid var(--cq-success);
    background: rgba(34, 197, 94, .10);
    color: #bbf7d0;
}

.bulk-message.error {
    border: 1px solid var(--cq-danger);
    background: rgba(251, 113, 133, .10);
    color: #fecdd3;
}

.bulk-note {
    margin: 10px 0 0;
    color: var(--cq-muted);
    font-size: .78rem;
    line-height: 1.6;
}

.bulk-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 12px 18px;
    margin-top: 24px;
}

.bulk-actions a {
    display: inline-flex;
    align-items: center;
    min-height: 40px;
    color: #8cecff;
    font-size: .86rem;
    font-weight: 600;
}

.bulk-actions a:hover {
    color: #fff;
}

/* =========================================================
   LARGE TABLET / SMALL LAPTOP: <= 1024px
   ========================================================= */

@media (max-width: 1024px) {
    .container,
    .main-container,
    .page-content {
        padding: 20px;
    }

    .card-grid,
    .dashboard-grid,
    .missions-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 16px;
    }
}

/* =========================================================
   TABLET: <= 768px
   ========================================================= */

@media (max-width: 768px) {
    .container,
    .main-container,
    .page-content {
        padding: 16px;
    }

    nav,
    .navbar {
        padding: 14px 18px;
    }

    .nav-links {
        gap: 10px 14px;
    }

    .bulk-page {
        padding: 28px 16px;
    }

    .bulk-card {
        max-width: 680px;
        border-radius: 18px;
    }

    .bulk-header {
        margin-bottom: 24px;
    }

    .bulk-section {
        margin-bottom: 16px;
    }

    .result-card {
        margin-block: 24px;
    }
}

/* =========================================================
   MOBILE: <= 480px
   ========================================================= */

@media (max-width: 480px) {
    body {
        font-size: 14px;
    }

    .container,
    .main-container,
    .page-content {
        padding: 12px;
    }

    nav,
    .navbar {
        align-items: stretch;
        flex-direction: column;
        gap: 10px;
        padding: 12px;
    }

    .nav-links {
        width: 100%;
        align-items: stretch;
        flex-direction: column;
        gap: 0;
    }

    .nav-links a {
        display: block;
        padding: 10px 8px;
        border-top: 1px solid var(--cq-border);
    }

    .card-grid,
    .dashboard-grid,
    .missions-grid {
        grid-template-columns: minmax(0, 1fr);
        gap: 12px;
    }

    .card,
    .dashboard-card,
    .mission-card,
    .form-card {
        padding: 16px;
        border-radius: 12px;
    }

    .bulk-page {
        padding: 14px 9px;
    }

    .bulk-card {
        padding: 21px 15px;
        border-radius: 15px;
    }

    .bulk-icon {
        width: 56px;
        height: 56px;
        margin-bottom: 14px;
        border-radius: 16px;
        font-size: 25px;
    }

    .bulk-header {
        margin-bottom: 22px;
    }

    .bulk-header h1 {
        font-size: 1.55rem;
    }

    .bulk-header > p:last-child,
    .bulk-description {
        font-size: .84rem;
    }

    .bulk-section {
        padding: 15px 12px;
        margin-bottom: 14px;
        border-radius: 12px;
    }

    .bulk-section h2 {
        font-size: 1rem;
    }

    .type-list {
        gap: 7px;
    }

    .type-badge {
        min-height: 31px;
        padding: 6px 9px;
        font-size: .72rem;
    }

    .bulk-form input[type="file"] {
        padding: 10px;
        font-size: .78rem;
    }

    .bulk-form input[type="file"]::file-selector-button {
        display: block;
        margin: 0 0 9px;
        font-size: .78rem;
    }

    .bulk-submit,
    button,
    .btn,
    .button {
        width: 100%;
        min-height: 44px;
        padding: 12px 14px;
    }

    .bulk-actions {
        align-items: stretch;
        flex-direction: column;
        gap: 8px;
        text-align: center;
    }

    .bulk-actions a {
        justify-content: center;
        padding: 8px;
    }

    input:not([type="checkbox"]):not([type="radio"]),
    select,
    textarea {
        font-size: 16px;
    }

    .result-card {
        width: calc(100% - 24px);
        margin: 18px auto;
        padding: 16px;
    }

    .result-actions {
        flex-direction: column;
        align-items: stretch;
    }

    .table-container {
        overflow-x: auto;
        -webkit-overflow-scrolling: touch;
    }

    table {
        min-width: 540px;
    }

    th,
    td {
        padding: 9px;
        font-size: .78rem;
    }

    pre {
        max-width: 100%;
        overflow-x: auto;
        font-size: .78rem;
    }
}

/* =========================================================
   SMALL MOBILE: <= 360px
   ========================================================= */

@media (max-width: 360px) {
    .container,
    .main-container,
    .page-content {
        padding: 8px;
    }

    .bulk-page {
        padding: 9px 6px;
    }

    .bulk-card {
        padding: 16px 11px;
    }

    .bulk-header h1 {
        font-size: 1.35rem;
    }

    .bulk-section {
        padding: 12px 10px;
    }

    .type-list {
        gap: 6px;
    }

    .type-badge {
        padding: 6px 8px;
        font-size: .68rem;
    }

    .bulk-actions a {
        font-size: .8rem;
    }
}

/* Respect reduced-motion preferences */
@media (prefers-reduced-motion: reduce) {
    *,
    *::before,
    *::after {
        scroll-behavior: auto !important;
        transition-duration: .01ms !important;
        animation-duration: .01ms !important;
        animation-iteration-count: 1 !important;
    }
}
