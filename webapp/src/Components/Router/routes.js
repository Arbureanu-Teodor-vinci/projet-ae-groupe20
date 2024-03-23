import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import HomePage from '../Pages/HomePage';
import ProfilPage from '../Pages/ProfilPage';
import ModificationPage from '../Pages/ModificationPage';
import AllUsersPage from '../Pages/AllUsersPage';

const routes = {
    '/': HomePage, // Default route, equivalent to '
    '/login': LoginPage,
    '/register': RegisterPage,
    '/profil': ProfilPage,
    '/modification': ModificationPage,
    '/allusers': AllUsersPage
};

export default routes;