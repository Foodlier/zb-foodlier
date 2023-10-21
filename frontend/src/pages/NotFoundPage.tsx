import * as S from '../styles/NotFoundPage.styled'
import BottomNavigation from '../components/BottomNavigation'
import Header from '../components/Header'

const NotFoundPage = () => {
  return (
    <>
      <Header />
      <S.Container>
        <S.Title>존재하지 않는 페이지입니다.</S.Title>
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default NotFoundPage
