import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import HomePage from '../Pages/HomePage';
import ProfilPage from '../Pages/ProfilPage';
import UpdateUserInfosPage from '../Pages/UpdateUserInfosPage';
import AllUsersPage from '../Pages/AllUsersPage';
import CreationContactPage from '../Pages/CreationContactPage';
import CreationCompanyPage from '../Pages/CreationCompanyPage';
import CreationInternshipPage from '../Pages/CreationInternshipPage';
import UpdateContactPage from '../Pages/UpdateContactPage';
import BoardPage from '../Pages/BoardPage';
import EnterprisePage from '../Pages/EnterprisePage';
import CreationSupervisorPage from '../Pages/CreationSupervisorPage';
import StudentInfoPage from '../Pages/StudentInfoPage';
import AllStudentsPage from '../Pages/AllStudentsPage';

const routes = {
    '/': HomePage,
    '/login': LoginPage,
    '/register': RegisterPage,
    '/profil': ProfilPage,
    '/updateUserInfos': UpdateUserInfosPage,
    '/allUsers': AllUsersPage,
    '/creationContact': CreationContactPage,
    '/creationCompany': CreationCompanyPage,
    '/creationStage': CreationInternshipPage,
    '/updateContact': UpdateContactPage,
    '/board': BoardPage,
    '/enterprise': EnterprisePage,
    '/creationSupervisor': CreationSupervisorPage,
    '/studentInfo': StudentInfoPage,
    '/allStudents': AllStudentsPage
};

export default routes;