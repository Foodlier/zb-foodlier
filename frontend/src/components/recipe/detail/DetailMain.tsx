import React from 'react'
import * as S from '../../../styles/recipe/detail/DetailMain.styled'
import { palette } from '../../../constants/Styles'
import useIcon from '../../../hooks/useIcon'

interface DetailMain {
  recipeId: string
  memberId: string
  nickname: string
  profileUrl: string
  title: string
  content: string
  mainImageUrl: string
  difficulty: string
  expectedTime: string
}

interface DetailMainProps {
  recipe: DetailMain
}

function DetailMainItem({ recipe }: DetailMainProps) {
  const { IcExpandRight, IcExportLight, IcTimeLight } = useIcon()

  return (
    <>
      {/* <p>레시피 번호: {recipe.recipeId}</p> {recipe.memberId} */}
      <S.Container>
        <S.LeftWrap>
          <S.MainImgWrap>
            <S.MainImg src={recipe.mainImageUrl} alt={recipe.title} />
          </S.MainImgWrap>
          <S.ProfileWrap>
            <S.Profile>
              <S.ProfileImg src={recipe.profileUrl} alt={recipe.nickname} />
              <S.ProfileId>{recipe.nickname}</S.ProfileId>
            </S.Profile>
            <IcExpandRight size={2.5} color={palette.textSecondary} />
          </S.ProfileWrap>
        </S.LeftWrap>

        <S.RightWrap>
          <S.Info>
            <S.DifficultyInfo>
              <S.InfoTit>난이도</S.InfoTit>
              <S.InfoTxt>{recipe.difficulty}</S.InfoTxt>
            </S.DifficultyInfo>
            <S.TimeInfo>
              <S.InfoTit>조리시간</S.InfoTit>
              <S.InfoTxt>
                <IcTimeLight size={2} color={palette.textSecondary} />
                {recipe.expectedTime}
              </S.InfoTxt>
            </S.TimeInfo>
          </S.Info>
          <S.MainTit>
            <h1>{recipe.title}</h1>
            <IcExportLight size={3.5} color={palette.textSecondary} />
          </S.MainTit>
          <S.MainTxt>{recipe.content}</S.MainTxt>
        </S.RightWrap>
      </S.Container>
    </>
  )
}

export default DetailMainItem
