import Slider, { Settings } from 'react-slick'
import 'slick-carousel/slick/slick.css'
import 'slick-carousel/slick/slick-theme.css'
import styled from 'styled-components'
import SliderCard from './SliderCard'
import { palette } from '../../constants/Styles'
import { RecipeListItem } from '../../constants/Interfaces'

interface SlickSliderProps {
  slides: RecipeListItem[]
}

const SlickSliderContainer = styled.div`
  width: 100vw;
  overflow: hidden;
  position: relative;
  left: 50%;
  transform: translate(-50%);

  & > div {
    display: flex;
    margin-bottom: 10rem;
  }
`

const CustomSlider = styled(Slider)`
  .slick-slider {
    display: flex;
    align-items: center;
    justify-content: center;
    width: auto;
  }
  .slick-list {
    margin-bottom: 1rem;
  }
  .slick-slide > div {
    width: 100%;
    padding: 3rem 0 2rem;
    transform: scale(0.8);
    box-sizing: border-box;
    transition: 1s;
  }

  .slick-center > div {
    transform: scale(1.05);
    padding: 3rem 0 2rem;
  }

  .slick-dots li {
    width: 1.5rem;
    height: 1.5rem;
    margin: 0 0.5rem;
  }

  .slick-dots li button:before {
    color: ${palette.main};
    font-size: 1.3rem;
  }
`

function SlickSlider({ slides }: SlickSliderProps) {
  const settings: Settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    centerMode: true,
    centerPadding: '20%',
    autoplay: true,
    autoplaySpeed: 3000,
  }

  return (
    <SlickSliderContainer>
      <CustomSlider
        dots={settings.dots}
        infinite={settings.infinite}
        speed={settings.speed}
        slidesToShow={settings.slidesToShow}
        centerMode={settings.centerMode}
        centerPadding={settings.centerPadding}
        autoplay={settings.autoplay}
        autoplaySpeed={settings.autoplaySpeed}
      >
        {slides.map(slideItem => (
          <SliderCard key={slideItem.recipeId} item={slideItem} />
        ))}
      </CustomSlider>
    </SlickSliderContainer>
  )
}

export default SlickSlider
