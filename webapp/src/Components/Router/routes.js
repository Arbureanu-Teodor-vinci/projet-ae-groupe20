import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import HomePage from '../Pages/HomePage';
import ProfilPage from '../Pages/ProfilPage';
import Logout from '../Logout/Logout';

const routes = {
    '/': HomePage, // Default route, equivalent to '
    '/login': LoginPage,
    '/register': RegisterPage,
    '/profil': ProfilPage,
    '/logout': Logout
};

export default routes;