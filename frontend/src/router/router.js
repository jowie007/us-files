// Router-Funktionen importieren
// import { createWebHashHistory, createRouter } from "vue-router";
import { createWebHashHistory, createRouter } from "vue-router";
import HomePage from "../components/HomePage";
import LoginPage from "../components/LoginPage";
import SongPage from "../components/SongPage";
import AboutPage from "../components/AboutPage";
import NewsPage from "../components/NewsPage";
import RegisterPage from "../components/RegisterPage";
import ProfilePage from "../components/ProfilePage";
import SearchPage from "../components/SearchPage";
import SongManagementPage from "../components/SongManagementPage";
import SongCalendar from "../components/SongCalendar";
import ErrorComponent from "../components/elements/ErrorComponent";
// Routen konfigurieren
const routes = [
    { path: "/", component: HomePage, name: "Home"},
    { path: "/about", component: AboutPage, name: "About"},
    { path: "/login", component: LoginPage, name: "Login"},
    { path: "/news", component: NewsPage, name: "News"},
    { path: "/register", component: RegisterPage, name: "Register"},
    { path: "/user/:name", component: ProfilePage, name: "Profile"},
    // https://stackoverflow.com/questions/45151810/passing-props-with-programmatic-navigation-vue-js
    { path: "/song/:id", component: SongPage, name: "", props: true},
    { path: "/song/edit/:id", component: SongManagementPage, name: "Edit song"},
    { path: "/song/new", component: SongManagementPage, name: "Create new song"},
    { path: "/search", component: SearchPage, name: "Search", props: true},
    { path: "/calendar", component: SongCalendar, name: "Song calendar"},
    // https://stackoverflow.com/questions/40193634/vue-router-redirect-on-page-not-found-404
    { path: '/:pathMatch(.*)*', component: ErrorComponent, name: "Error" }
];
// Router-Objekt initialisieren
const router = createRouter({
    history: createWebHashHistory(),
    // history: createWebHashHistory(),
    // mode: 'history',
    routes,
});
// Router-Objekt exportieren
export default router;