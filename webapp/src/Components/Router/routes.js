import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import HomePage from '../Pages/HomePage';
import ProfilPage from '../Pages/ProfilPage';
import ModificationPage from '../Pages/ModificationPage';
import AllUsersPage from '../Pages/AllUsersPage';
import CreationContactPage from '../Pages/CreationContactPage';
import CreationCompanyPage from '../Pages/CreationCompanyPage';
import UpdateContactPage from '../Pages/UpdateContactPage';

const routes = {
    '/': HomePage, // Default route, equivalent to '
    '/login': LoginPage,
    '/register': RegisterPage,
    '/profil': ProfilPage,
    '/modification': ModificationPage,
    '/allUsers': AllUsersPage,
    '/creationContact': CreationContactPage,
    '/creationCompany': CreationCompanyPage,
    '/updateContact': UpdateContactPage
};

export default routes;