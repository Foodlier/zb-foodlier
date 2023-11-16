import { useNavigate } from 'react-router-dom'
import * as S from '../../../styles/recipe/detail/DetailMain.styled'
import { palette } from '../../../constants/Styles'
import useIcon from '../../../hooks/useIcon'
import { Recipe } from '../../../constants/Interfaces'
import profile from '../../../../public/images/default_profile.png'

interface DetailMainProps {
  recipe: Recipe | undefined
  activeModal: () => void
}

const DetailMainItem = ({ recipe, activeModal }: DetailMainProps) => {
  const navigate = useNavigate()
  const { IcExpandRight, IcTimeLight } = useIcon()

  const TOKEN: string | null = JSON.parse(
    localStorage.getItem('accessToken') ?? 'null'
  )

  const goToProfile = () => {
    if (TOKEN) {
      navigate(`/profile/${recipe?.memberId}`)
    } else {
      activeModal()
    }
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
          </S.MainTit>
          <S.MainTxt>{recipe.content}</S.MainTxt>
        </S.RightWrap>
      </S.Container>
    </>
  )
}

export default DetailMainItem
