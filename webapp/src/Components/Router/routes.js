import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import HomePage from '../Pages/HomePage';
import ProfilPage from '../Pages/ProfilPage';
import ModificationPage from '../Pages/ModificationPage';
import AllUsersPage from '../Pages/AllUsersPage';
import CreationContactPage from '../Pages/CreationContactPage';
import CreationCompanyPage from '../Pages/CreationCompanyPage';
import CreationStagePage from '../Pages/CreationStagePage';
import UpdateContactPage from '../Pages/UpdateContactPage';
import BoardPage from '../Pages/BoardPage';
import EnterprisePage from '../Pages/EnterprisePage';
import CreationSupervisorPage from '../Pages/CreationSupervisorPage';

const routes = {
    '/': HomePage, // Default route, equivalent to '
    '/login': LoginPage,
    '/register': RegisterPage,
    '/profil': ProfilPage,
    '/modification': ModificationPage,
    '/allUsers': AllUsersPage,
    '/creationContact': CreationContactPage,
    '/creationCompany': CreationCompanyPage,
    '/creationStage': CreationStagePage,
    '/updateContact': UpdateContactPage,
    '/board': BoardPage,
    '/enterprise': EnterprisePage,
    '/creationSupervisor': CreationSupervisorPage
};

export default routes;