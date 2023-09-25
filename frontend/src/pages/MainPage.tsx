import styled from 'styled-components'
import SlickSlider from '../components/slider/Slider'
import { slides } from '../components/slider/Slide'

const SlickContainer = styled.section`
  margin-top: 5rem;
`

const Tit = styled.h1`
  padding-left: 2rem;
  font-size: 2.5rem;
  font-family: 'Pretendard-ExtraBold';

  @media screen and (min-width: 768px) {
    padding-left: 2%;
    font-size: 3rem;
  }
`

const MainPage = () => {
  return (
    <SlickContainer>
      <Tit>오늘 이 요리 어때요?</Tit>
      <SlickSlider slides={slides} />
    </SlickContainer>
  )
}

export default MainPage
