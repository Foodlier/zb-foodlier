import { useNavigate } from 'react-router-dom'
import * as S from '../../../styles/recipe/detail/DetailMain.styled'
import { palette } from '../../../constants/Styles'
import useIcon from '../../../hooks/useIcon'
import { Recipe } from '../../../constants/Interfaces'
import profile from '../../../../public/images/default_profile.png'

interface DetailMainProps {
  recipe: Recipe | undefined
}

const DetailMainItem = ({ recipe }: DetailMainProps) => {
  const navigate = useNavigate()
  const { IcExpandRight, IcExportLight, IcTimeLight } = useIcon()

  const goToProfile = () => {
    // 정확한 경로로 router 수정
    navigate(`profile/${recipe?.memberId}`)
  }

  if (!recipe) return null

  return (
    <>
      {/* <p>레시피 번호: {recipe.recipeId}</p> {recipe.memberId} */}
      <S.Container>
        <S.LeftWrap>
          <S.MainImgWrap>
            <S.MainImg src={recipe.mainImageUrl} alt={recipe.title} />
          </S.MainImgWrap>
          <S.ProfileWrap onClick={goToProfile}>
            <S.Profile>
              <S.ProfileImg
                src={recipe.profileUrl || profile}
                alt={recipe.nickname}
              />
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
              <IcTimeLight size={2} color={palette.textSecondary} />
              <S.InfoTxt>{`${recipe.expectedTime}분`}</S.InfoTxt>
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
