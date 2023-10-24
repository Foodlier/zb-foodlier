/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import * as S from '../../styles/user/MyLogPage.styled'
import BottomNavigation from '../../components/BottomNavigation'
import Header from '../../components/Header'
import axiosInstance from '../../utils/FetchCall'
import RecipeItem from '../../components/recipe/RecipeItem'
import { Comment, RecipeListItem } from '../../constants/Interfaces'
import CommentItem from '../../components/auth/CommentItem'

const MyLogPage = () => {
  const navigate = useNavigate()
  const { category } = useParams() as {
    category: 'recipe' | 'comment' | 'like'
  }

  const localProfile = localStorage.getItem('PROFILE')
  const profile = localProfile ? JSON.parse(localProfile) : null

  const CATEGORY_TYPE = {
    recipe: '작성한 꿀조합 게시물',
    comment: '작성한 댓글',
    like: '좋아요 한 게시물',
  } as const

  const [totalCount, setTotalCount] = useState(0)

  const [myRecipeList, setMyRecipeList] = useState<RecipeListItem[]>([])
  const [myCommentList, setMyCommentList] = useState<Comment[]>([])
  const [myLikeList, setMyLikeList] = useState<RecipeListItem[]>([])

  const getMyRecipe = async () => {
    if (!profile) return
    const pageIdx = 0
    const pageSize = 20

    const res = await axiosInstance.get(
      `/api/profile/public/recipe/${pageIdx}/${pageSize}/${profile.myMemberId}`
    )

    setMyRecipeList(res.data.content)
    setTotalCount(res.data.totalElements)
  }

  const getMyComment = async () => {
    const pageIdx = 0
    const pageSize = 20

    const res = await axiosInstance.get(
      `/api/profile/private/comment/${pageIdx}/${pageSize}`
    )
    setMyCommentList(res.data.content)
    setTotalCount(res.data.totalElements)
  }

  const getMyLike = async () => {
    const pageIdx = 0
    const pageSize = 20

    const res = await axiosInstance.get(
      `/api/profile/private/heart/${pageIdx}/${pageSize}`
    )
    setMyLikeList(res.data.content)
    setTotalCount(res.data.totalElements)
  }

  useEffect(() => {
    if (category === 'recipe') {
      getMyRecipe()
    } else if (category === 'comment') {
      getMyComment()
    } else if (category === 'like') {
      getMyLike()
    }
  }, [])

  if (!Object.keys(CATEGORY_TYPE).includes(category)) return null

  return (
    <>
      <Header />
      <S.Container>
        <S.WrapTitle>
          <S.Title>{`내가 ${CATEGORY_TYPE[category]}`}</S.Title>
          <S.SubTitle>{`총 ${totalCount}개`}</S.SubTitle>
        </S.WrapTitle>
        <S.WrapList>
          {category === 'recipe' &&
            myRecipeList.map(myRecipeItem => (
              <RecipeItem
                key={myRecipeItem.recipeId}
                recipeItem={myRecipeItem}
                onClick={() => {
                  navigate(`/recipe/detail/${myRecipeItem.recipeId}`)
                }}
              />
            ))}
          {category === 'comment' &&
            myCommentList.map(commentItem => (
              <CommentItem key={commentItem.createdAt} item={commentItem} />
            ))}

          {category === 'like' &&
            myLikeList.map(likeItem => (
              <RecipeItem
                key={likeItem.recipeId}
                recipeItem={likeItem}
                onClick={() => {
                  navigate(`/recipe/detail/${likeItem.recipeId}`)
                }}
              />
            ))}
        </S.WrapList>
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default MyLogPage
