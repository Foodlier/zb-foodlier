import { useState } from 'react'
import * as S from '../../styles/refrigerator/CookForYou.styled'

const CookForYou = () => {
  const [optionToggle, setOptionToggle] = useState(false)
  const [option, setOption] = useState<string | null>('거리 순')
  const optionHandler = (e: React.MouseEvent) => {
    const target = e.target as HTMLButtonElement
    setOption(target.textContent)
    setOptionToggle(!optionToggle)
  }

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
    <>
      <S.ChefListContainer>
        <S.Info>
          <S.SubTitle>내 주변 요청</S.SubTitle>
          <S.SelectBox>
            <S.SelectedBox>
              <S.OptionButton
                onClick={() => {
                  setOptionToggle(!optionToggle)
                }}
              >
                <div>{option}</div>
                <div>▽</div>
              </S.OptionButton>
              <S.OptionList $toggle={optionToggle}>
                {OPTION_MENU_LIST.map(el => (
                  <S.Option key={el}>
                    <S.OptionButton type="button" onClick={optionHandler}>
                      {el}
                    </S.OptionButton>
                  </S.Option>
                ))}
              </S.OptionList>
            </S.SelectedBox>
          </S.SelectBox>
        </S.Info>
        <S.CardList>
          {CHEF_LIST_EXAMPLE.map(el => (
            <S.Card key={el.nickName}>
              <S.CardInfo className="card-info">
                <S.MainImg src="" alt="대표 사진" className="mainImg" />
                <S.ChefInfo className="chef-info">
                  <S.ChefTopInfo className="top-info">
                    <span className="nickName">{el.nickName}</span>
                  </S.ChefTopInfo>
                  <S.ChefBottomInfo className="bottom-info">
                    {el.description}
                  </S.ChefBottomInfo>
                </S.ChefInfo>
              </S.CardInfo>
              <S.ElseInfo>
                <span>{el.distance}m</span>
                <S.RequestButton type="button">요청서 보기</S.RequestButton>
              </S.ElseInfo>
            </S.Card>
          ))}
        </S.CardList>
      </S.ChefListContainer>
      <S.SpaceDiv />
    </>
  )
}

export default CookForYou
