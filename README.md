# Gaurav Wellness Centre – Client Manager (Android App)

Native Android app (Kotlin + Jetpack Compose + Room) built to match the
"Gaurav Wellness Centre" client intake/tracking form.

## Features
- **Add unlimited clients** with Personal Information (name, mobile, DOB, age,
  gender, height, address, occupation, goal, consultant) and Lifestyle
  Information (wake-up time, exercise, water intake, diet type, meals, sleep).
- **Multiple visits per client** — full Body Assessment Tracker: Weight, BMI,
  Body Fat %, Muscle %, Visceral Fat, Subcutaneous Fat, BMR, Body Age,
  Hydration %, Protein %, Bone Mass, Metabolic Age, Waist, Hip, Chest, Arm,
  Thigh.
- **Progress Notes** log (diet changes, exercise changes, remarks).
- **Charts per client**: Weight / BMI / Body Fat % trend lines across visits.
- **Dashboard**: goal distribution pie chart + latest-weight bar chart
  comparing all clients at once.
- **PDF export**: generates a clinic-style PDF report per client (info +
  full visit history table + notes).
- **Share & Print**: share the PDF via any app, or send directly to a
  connected/Bluetooth/cloud printer through Android's print dialog.
- **100% offline** — all data stored locally with Room (SQLite). No internet
  or login required.

## How to open & run
1. Install **Android Studio** (Koala or newer recommended):
   https://developer.android.com/studio
2. Open Android Studio → **Open** → select the `ClientManager` folder
   (this project).
3. Let Gradle sync finish (it will download dependencies the first time —
   needs internet).
4. Connect a phone (USB debugging on) or start an emulator.
5. Click **Run ▶** (or `Shift+F10`).

## Project structure
```
app/src/main/java/com/example/clientmanager/
├── data/                  # Room entities, DAOs, database, repository
├── ui/
│   ├── screens/           # All app screens (list, add, detail, charts, PDF)
│   ├── components/        # Reusable chart composables
│   ├── theme/              # Colors & Material3 theme
│   └── *ViewModel.kt      # State holders per screen
├── util/PdfExporter.kt    # PDF generation + print logic
└── MainActivity.kt        # Entry point
```

## Customizing
- **App name / package**: change `applicationId` in `app/build.gradle.kts`
  and the `namespace`.
- **Colors**: edit `ui/theme/Color.kt` (currently matches the wellness
  centre's green branding).
- **Add more body-assessment fields**: extend `data/Visit.kt`, then add a
  matching field in `AddVisitScreen.kt` and `PdfExporter.kt`.
- **Real launcher logo**: replace the vector in
  `res/drawable/ic_launcher_foreground.xml` with the centre's actual logo
  (export as a 108x108dp vector or PNG mipmaps).

## Option B: Build the APK without installing anything (GitHub Actions)

If your computer can't run Android Studio, you can build the APK entirely
in the cloud using GitHub — you only need a browser.

1. Go to https://github.com and create a free account (if you don't have one).
2. Click **New repository** → name it e.g. `client-manager` → set to
   **Public** (or Private, both work) → **Create repository**.
3. On the new repo page, click **uploading an existing file**.
4. Drag and drop **all the contents of this folder** (not the zip itself —
   extract it first, then drag the files/folders inside) into the upload
   box. Commit the changes.
5. Go to the **Actions** tab of your repository. A workflow called
   **"Build APK"** will start automatically (or click **Run workflow** if it
   doesn't).
6. Wait 2–4 minutes for it to finish (green checkmark ✅).
7. Click on the finished run → scroll to **Artifacts** → download
   **app-debug-apk**. This is a zip containing your `app-debug.apk`.
8. Transfer that APK to your phone (via WhatsApp/Google Drive/USB/email)
   and tap it to install (you may need to allow "install from unknown
   sources" once, Android will prompt you automatically).

That's it — no Android Studio, no local build tools needed.


- Minimum Android version supported: **Android 7.0 (API 24)**.
- Data is private to the device (Room/SQLite) — no cloud sync in this
  version. If you later want cross-device sync/backup, Firebase Firestore
  can be added on top of the existing `WellnessRepository` without changing
  the UI layer.
