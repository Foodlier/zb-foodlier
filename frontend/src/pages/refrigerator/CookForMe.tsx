import { useState } from 'react'
import styled from 'styled-components'
// import { breakpoints } from '../constants/Styles'

// 스타일드 컴포넌트
const Container = styled.div`
  width: 100%;
  background-color: aliceblue;
`

const Map = styled.div`
  width: 100%;
  height: 440px;
  background-color: #b7ddff;
`

const ChefListContainer = styled.div`
  padding: 20px 20px 0 20px;
  @media (min-width: 768px) {
    padding-bottom: 93px;
  }
`
const Info = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`
const SubTitle = styled.span`
  font-weight: bold;
  font-size: 2rem;
`

const SelectBox = styled.div`
  width: 150px;
  height: 100%;

  box-sizing: border-box;
  text-align: left;
`

const SelectedBox = styled.div`
  position: relative;
  font-weight: bold;
  font-size: 2rem;
`

const OptionList = styled.ul<{ $toggle: boolean }>`
  display: ${props => (props.$toggle ? 'block' : 'none')};
  width: 100%;
  position: absolute;
  top: 30px;
  left: 0;
  background-color: #ffffff;
  border: 1px solid #d9d9d9;
  padding-left: 0;
  border-radius: 5px;
`

const Option = styled.li`
  &:hover {
    background-color: #ececec;
  }
`

const OptionButton = styled.button`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  text-align: right;
  padding: 0 4px;
`
const CardList = styled.ul`
  width: 100%;
  height: 500px;
  overflow: auto;
  padding-left: 0;
`

const Card = styled.li`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  height: 220px;
  padding: 20px;
  box-sizing: border-box;
  margin-bottom: 10px;
  border: 1px solid #d9d9d9;
  border-radius: 10px;
`
const CardInfo = styled.div`
  display: flex;
  gap: 20px;
  .mainImg {
    width: 90px;
    height: 90px;
    background-color: yellowgreen;
    border-radius: 10px;
  }
`

const ChefInfo = styled.div`
  display: flex;
  flex-direction: column;
  padding-top: 10px;
  gap: 16px;
`

const ChefTopInfo = styled.div`
  display: flex;
  align-items: center;
  height: 20px;
  .nickName {
    font-size: 20px;
    font-weight: bold;
  }
`

const ChefBottomInfo = styled.p`
  max-width: 188px;
`
const ElseInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: end;
`

const RequestButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: #e45141;
  color: #ffffff;
  border-radius: 5px;
`

const WritingButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: #e45141;
  color: #ffffff;
  border-radius: 5px;
`

const ButtonList = styled.div`
  display: flex;
  justify-content: center;
  gap: 10px;
  border-top: 1px solid #d9d9d9d9;
  padding: 10px 20px 0 0;
  @media (min-width: 768px) {
    display: flex;
    justify-content: end;
  }
  button {
    margin-bottom: 110px;
    @media (min-width: 768px) {
      margin-bottom: 0px;
    }
  }
`

const CookForMe = () => {
  const [optionToggle, setOptionToggle] = useState(false)
  const [option, setOption] = useState<string | null>('거리 순')
  const optionHandler = (e: React.MouseEvent) => {
    const target = e.target as HTMLButtonElement
    setOption(target.textContent)
    setOptionToggle(!optionToggle)
  }

  // const [requestToggle, setRequestToggle] = useState(false)
  // const requestHandler = (e: React.MouseEvent) => {
  //   const target = e.target as HTMLButtonElement
  //   setRequestToggle(!requestToggle)
  // }

  const OPTION_MENU_LIST = [
    '거리 순',
    '평점 순',
    '리뷰 많은 순',
    '레시피 많은 순',
  ]

  const CHEF_LIST_EXAMPLE = [
    {
      nickName: '나는 요리사',
      rating: 2.1,
      description: '대존맛 호랑이 구이 레시피',
      distance: 400,
      reviewCount: 15,
    },
    {
      nickName: '나는 요리사2',
      rating: 0.4,
      description: '대존맛 코끼리 간장 구이 레시피',
      distance: 100,
      reviewCount: 15,
    },
    {
      nickName: '나는 요리사3',
      rating: 4.7,
      description: '대존맛 맷비둘기 구이 레시피',
      distance: 300,
      reviewCount: 15,
    },
    {
      nickName: '나는 요리사4',
      rating: 3.5,
      description: '대존맛 고라니 구이 레시피',
      distance: 180,
      reviewCount: 15,
    },
  ]

  const BUTTON_LIST = ['+ 요청서 작성', '요청서 목록']

  return (
    <Container>
      <Map />
      <ChefListContainer>
        <Info>
          <SubTitle>내 주변 요리사</SubTitle>
          <SelectBox>
            <SelectedBox>
              <OptionButton
                onClick={() => {
                  setOptionToggle(!optionToggle)
                }}
              >
                <div>{option}</div>
                <div>v</div>
              </OptionButton>
              <OptionList $toggle={optionToggle}>
                {OPTION_MENU_LIST.map(el => (
                  <Option key={el}>
                    <OptionButton type="button" onClick={optionHandler}>
                      {el}
                    </OptionButton>
                  </Option>
                ))}
              </OptionList>
            </SelectedBox>
          </SelectBox>
        </Info>
        <CardList>
          {CHEF_LIST_EXAMPLE.map(el => (
            <Card key={el.nickName}>
              <CardInfo className="card-info">
                <img src="" alt="대표 사진" className="mainImg" />
                <ChefInfo className="chef-info">
                  <ChefTopInfo className="top-info">
                    <span className="nickName">{el.nickName}</span>
                    <img src="" alt="평점" className="star" />
                    <span className="rating">{`${el.rating}(${el.reviewCount})`}</span>
                  </ChefTopInfo>
                  <ChefBottomInfo className="bottom-info">
                    {el.description}
                  </ChefBottomInfo>
                </ChefInfo>
              </CardInfo>
              <ElseInfo>
                <span>{el.distance}m</span>
                <RequestButton type="button">요청하기</RequestButton>
              </ElseInfo>
            </Card>
          ))}
        </CardList>
        <ButtonList>
          {BUTTON_LIST.map(el => (
            <WritingButton key={el} type="button" className="d">
              {el}
            </WritingButton>
          ))}
        </ButtonList>
      </ChefListContainer>
    </Container>
  )
}

export default CookForMe
