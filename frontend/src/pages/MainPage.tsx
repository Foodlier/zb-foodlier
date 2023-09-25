import styled from 'styled-components'
import SlickSlider from '../components/slider/Slider'
import { slides } from '../components/slider/Slide'
import WebSearch from '../components/search/WebSearch'
import { innerWidth } from '../constants/Styles'

const SlickContainer = styled.section`
  margin-top: 5rem;
`

const Tit = styled.h1`
  ${innerWidth}
  font-size: 2.5rem;
  font-family: 'Pretendard-ExtraBold';

  @media screen and (min-width: 768px) {
    font-size: 3rem;
  }
`

const MainPage = () => {
  const handleSearch = (query: string) => {
    console.log(`검색어: ${query}`)
  }

  return (
    <>
      <WebSearch onSearch={handleSearch} />
      <SlickContainer>
        <Tit>오늘 이 요리 어때요?</Tit>
        <SlickSlider slides={slides} />
      </SlickContainer>
    </>
  )
}

export default MainPage
