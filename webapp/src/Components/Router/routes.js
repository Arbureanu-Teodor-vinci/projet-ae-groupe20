import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import HomePage from '../Pages/HomePage';
import ChoicePage from '../Pages/ChoicePage';
import StudentPage from '../Pages/StudentPage';

const routes = {
    '/': HomePage, // Default route, equivalent to '
    '/login': LoginPage,
    '/register': RegisterPage,
    '/choice': ChoicePage,
    '/student': StudentPage
};

export default routes;