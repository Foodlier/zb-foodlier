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
`

const MainImg = styled.img`
  min-width: 90px;
  height: 90px;
  background-color: yellowgreen;
  border-radius: 10px;
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
  border: 1px solid #e45141;
  color: #e45141;
  font-weight: bold;
  border-radius: 5px;
`

const CookForYou = () => {
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
      nickName: '나는 사용자',
      description: '호랑이 구이 만들어주세요',
      distance: 400,
      reviewCount: 15,
    },
    {
      nickName: '나는 사용자2',
      description: '코끼리 간장 구이 만들어주세요',
      distance: 100,
      reviewCount: 15,
    },
    {
      nickName: '나는 사용자3',
      description: '맷비둘기 구이 만들어주세요ㅇdddd',
      distance: 300,
      reviewCount: 15,
    },
    {
      nickName: '나는 사용자4',
      description: '고라니 구이 만들어주세요',
      distance: 180,
      reviewCount: 15,
    },
  ]

  return (
    <Container>
      <Map />
      <ChefListContainer>
        <Info>
          <SubTitle>내 주변 요청</SubTitle>
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
                <MainImg src="" alt="대표 사진" className="mainImg" />
                <ChefInfo className="chef-info">
                  <ChefTopInfo className="top-info">
                    <span className="nickName">{el.nickName}</span>
                  </ChefTopInfo>
                  <ChefBottomInfo className="bottom-info">
                    {el.description}
                  </ChefBottomInfo>
                </ChefInfo>
              </CardInfo>
              <ElseInfo>
                <span>{el.distance}m</span>
                <RequestButton type="button">요청서 보기</RequestButton>
              </ElseInfo>
            </Card>
          ))}
        </CardList>
      </ChefListContainer>
    </Container>
  )
}

export default CookForYou
