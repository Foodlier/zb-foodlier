/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable no-restricted-globals */
/* eslint-disable react/no-array-index-key */
import { useLocation, useNavigate } from 'react-router-dom'
import React, { useEffect, useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/recipe/WriteRecipePage.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import RecipeImage, { ImageFile } from '../../components/recipe/RecipeImage'
import {
  DIFFICULTY_LIST,
  EMPTY_INGREDIENT,
  EMPTY_ORDER,
  INGREDIENT_LIST,
} from '../../constants/Data'
import axiosInstance, { postFormData } from '../../utils/FetchCall'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'
import {
  RecipeIngredientDtoList,
  RecipeDetailDtoList,
} from '../../constants/Interfaces'

interface Recipe {
  title: string
  content: string
  mainImageUrl: string
  recipeIngredientDtoList: RecipeIngredientDtoList[]
  difficulty: string
  recipeDetailDtoList: RecipeDetailDtoList[]
  expectedTime: number
}

const WriteRecipePage = () => {
  const navigate = useNavigate()
  const { state } = useLocation()
  const recipeId = state?.recipeId || 0

  const isEdit = Boolean(recipeId)

  const { IcAddRound } = useIcon()

  const emptyFile = new File([''], 'empty.txt', { type: 'text/plain' })

  const [imageFile, setImageFile] = useState<ImageFile>({
    mainImage: emptyFile,
    cookingOrderImageList: [],
  })
  const [isCompleteModal, setIsCompleteModal] = useState(false)
  const [modalContent, setModalContent] = useState('')
  const [errorValue, setErrorValue] = useState({
    title: '',
    content: '',
    mainImageUrl: '',
    recipeIngredientDtoList: '',
    difficulty: '',
    recipeDetailDtoList: '',
    expectedTime: '',
  })
  const [recipeValue, setRecipeValue] = useState<Recipe>({
    title: '',
    content: '',
    mainImageUrl: '',
    recipeIngredientDtoList: [{ ...EMPTY_INGREDIENT }],
    difficulty: '',
    recipeDetailDtoList: [{ ...EMPTY_ORDER }],
    expectedTime: 0,
  })

  // 레시피 재료 or 순서 추가
  const addItem = (key: string) => {
    if (key === 'ingredient') {
      if (recipeValue.recipeIngredientDtoList.length > 4) return
      const updateValue = [
        ...recipeValue.recipeIngredientDtoList,
        {
          ...EMPTY_INGREDIENT,
        },
      ]
      setRecipeValue({
        ...recipeValue,
        recipeIngredientDtoList: [...updateValue],
      })
    } else {
      const updateValue = [
        ...recipeValue.recipeDetailDtoList,
        { ...EMPTY_ORDER },
      ]
      setRecipeValue({
        ...recipeValue,
        recipeDetailDtoList: [...updateValue],
      })
    }
  }

  const deleteItem = (index: number, key: string) => {
    if (key === 'ingredient') {
      recipeValue.recipeIngredientDtoList.splice(index, 1)
    } else {
      recipeValue.recipeDetailDtoList.splice(index, 1)
    }
    setRecipeValue({ ...recipeValue })
  }

  // 해당 index의 재료 값 수정
  const updateIngredient = (
    e: React.ChangeEvent<HTMLInputElement>,
    index: number,
    category: string
  ) => {
    const updatedValue = [...recipeValue.recipeIngredientDtoList]
    if (category === 'count') {
      if (isNaN(Number(e.target.value))) return
      updatedValue[index][category] = Number(e.target.value)
    } else if (category === 'name' || category === 'unit') {
      updatedValue[index][category] = e.target.value
    }

    setRecipeValue(prevRecipeValue => ({
      ...prevRecipeValue,
      recipeIngredientDtoList: updatedValue,
    }))
  }

  // 해당 index의 순서 값 수정
  const updateOrder = (
    e: React.ChangeEvent<HTMLInputElement>,
    index: number
  ) => {
    const updatedValue = [...recipeValue.recipeDetailDtoList]

    updatedValue[index].cookingOrder = e.target.value

    setRecipeValue(prevRecipeValue => ({
      ...prevRecipeValue,
      recipeDetailDtoList: updatedValue,
    }))
  }

  const editRecipe = async () => {
    try {
      const { status } = await axiosInstance.put(
        `/api/recipe/${recipeId}`,
        recipeValue
      )
      if (status === 200) {
        setModalContent('게시글 수정이 완료되었습니다.')
      }
    } catch (error) {
      console.log(error)
      setModalContent('게시글 수정에 실패하였습니다.')
    }
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
    }, 1500)
  }

  const postRecipe = async (body: Recipe) => {
    try {
      const { status } = await axiosInstance.post('/api/recipe', body)
      if (status === 200) {
        setModalContent('게시글 작성이 완료되었습니다.')
      }
    } catch (error) {
      console.log(error)
      setModalContent('게시글 작성에 실패하였습니다.')
    }
    setIsCompleteModal(true)
    setTimeout(() => {
      setIsCompleteModal(false)
      navigate(-1)
    }, 1500)
  }

  const postImage = async () => {
    try {
      const formData = new FormData()
      formData.append('mainImage', imageFile.mainImage)
      imageFile.cookingOrderImageList.forEach(image =>
        formData.append('cookingOrderImageList', image)
      )

      const { data, status } = await postFormData('/api/recipe/image', formData)
      if (status === 200) {
        const body = { ...recipeValue }
        const { cookingOrderImageList, mainImage } = data

        cookingOrderImageList.forEach((image: string, index: number) => {
          body.recipeDetailDtoList[index].cookingOrderImageUrl = image
        })
        body.mainImageUrl = mainImage
        console.log(body)
        setRecipeValue(body)
        postRecipe(body)
      }
    } catch (error) {
      console.log(error)
    }
  }

  // 유효성 검사
  const checkForm = () => {
    const errors: Record<string, string> = { ...errorValue }

    const validations = [
      {
        condition: isEdit ? false : imageFile.mainImage.name === 'empty.txt',
        key: 'mainImageUrl',
        message: '대표 이미지를 선택해주세요.',
      },
      {
        condition:
          recipeValue.title.length < 2 || recipeValue.title.length > 20,
        key: 'title',
        message: '제목을 2글자 이상 20자 이하로 입력해주세요',
      },
      {
        condition: recipeValue.content.length < 2,
        key: 'content',
        message: '설명을 2글자 이상 입력해주세요',
      },
      {
        condition:
          recipeValue.recipeIngredientDtoList.filter(item => !item.name)
            .length > 0 ||
          recipeValue.recipeIngredientDtoList.filter(item => !item.count)
            .length > 0 ||
          recipeValue.recipeIngredientDtoList.filter(item => !item.unit)
            .length > 0,
        key: 'recipeIngredientDtoList',
        message: '재료를 1개 이상 입력해주세요.',
      },
      {
        condition: !recipeValue.difficulty,
        key: 'difficulty',
        message: '난이도를 선택해주세요',
      },
      {
        condition: !recipeValue.expectedTime,
        key: 'expectedTime',
        message: '조리시간을 입력해주세요',
      },
      {
        condition:
          (isEdit ? false : !imageFile.cookingOrderImageList.length) ||
          recipeValue.recipeDetailDtoList.filter(item => !item.cookingOrder)
            .length > 0,
        key: 'recipeDetailDtoList',
        message: '순서를 1개 이상 입력해주세요.',
      },
    ]
    validations.forEach(validation => {
      if (validation.condition) {
        errors[validation.key] = validation.message
      } else {
        errors[validation.key] = ''
      }
    })
    setErrorValue({ ...errorValue, ...errors })

    if (Object.values(errors).every(error => error === '')) {
      if (isEdit) {
        editRecipe()
      } else {
        postImage()
      }
    }
  }

  const getRecipe = async () => {
    const { data } = await axiosInstance.get(`/api/recipe/${recipeId}`)

    setRecipeValue({
      title: data.title,
      content: data.content,
      mainImageUrl: data.mainImageUrl,
      recipeIngredientDtoList: data.recipeIngredientDtoList,
      difficulty: data.difficulty,
      recipeDetailDtoList: data.recipeDetailDtoList,
      expectedTime: data.expectedTime,
    })
  }

  useEffect(() => {
    if (isEdit) {
      getRecipe()
    }
  }, [])

  return (
    <>
      <Header />
      <S.Container>
        <RecipeImage
          size={25}
          isText
          formKey="mainImage"
          imageFile={imageFile}
          setImageFile={setImageFile}
          defaultUrl={recipeValue.mainImageUrl}
        />
        <S.ErrorText>{errorValue.mainImageUrl}</S.ErrorText>

        <S.WrapForm>
          <S.Title>제목</S.Title>
          <S.Input
            placeholder="제목을 입력해주세요"
            value={recipeValue.title}
            onChange={e =>
              setRecipeValue({ ...recipeValue, title: e.target.value })
            }
          />
          <S.ErrorText>{errorValue.title}</S.ErrorText>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>설명</S.Title>
          <S.Input
            placeholder="설명을 입력해주세요"
            value={recipeValue.content}
            onChange={e =>
              setRecipeValue({ ...recipeValue, content: e.target.value })
            }
          />
          <S.ErrorText>{errorValue.content}</S.ErrorText>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>재료</S.Title>
          {recipeValue.recipeIngredientDtoList.map((item, index) => (
            <S.FlexWrap key={`key-${index}`}>
              <S.WrapIngredient>
                <S.WrapItemInput>
                  {INGREDIENT_LIST.map(ingredientItem => (
                    <S.ItemInput
                      key={ingredientItem.value}
                      placeholder={ingredientItem.placeholder}
                      onChange={e =>
                        updateIngredient(e, index, ingredientItem.value)
                      }
                      value={item[ingredientItem.value]}
                      $width={ingredientItem.width}
                      $radius={ingredientItem.radius}
                    />
                  ))}
                </S.WrapItemInput>
              </S.WrapIngredient>
              {Boolean(index) && (
                <S.DeleteItem onClick={() => deleteItem(index, 'ingredient')}>
                  삭제
                </S.DeleteItem>
              )}
            </S.FlexWrap>
          ))}
          <S.AddButton onClick={() => addItem('ingredient')} $width={50}>
            <IcAddRound size={1.2} color={palette.textSecondary} />
            재료 추가
          </S.AddButton>
          <S.ErrorText>{errorValue.recipeIngredientDtoList}</S.ErrorText>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>난이도</S.Title>
          <S.WrapDifficultyButton>
            {DIFFICULTY_LIST.map(item => (
              <S.Difficulty
                key={item.content}
                onClick={() =>
                  setRecipeValue({ ...recipeValue, difficulty: item.value })
                }
                $isActive={recipeValue.difficulty === item.value}
              >
                {item.content}
              </S.Difficulty>
            ))}
          </S.WrapDifficultyButton>

          <S.ErrorText>{errorValue.difficulty}</S.ErrorText>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>조리시간</S.Title>
          <S.WrapTime>
            <S.Input
              onChange={e => {
                if (isNaN(Number(e.target.value))) return
                setRecipeValue({
                  ...recipeValue,
                  expectedTime: Number(e.target.value),
                })
              }}
              value={recipeValue.expectedTime}
              $width={30}
              $marginRi={0.6}
            />
            분 이내
          </S.WrapTime>

          <S.ErrorText>{errorValue.expectedTime}</S.ErrorText>
        </S.WrapForm>

        <S.WrapForm>
          <S.Title>순서</S.Title>
          {recipeValue.recipeDetailDtoList.map((item, index) => (
            <S.FlexWrap key={`key-${index}`}>
              <S.WrapOrder>
                <RecipeImage
                  size={7}
                  isText={false}
                  formKey="cookingOrderImageList"
                  imageFile={imageFile}
                  setImageFile={setImageFile}
                  defaultUrl={item.cookingOrderImageUrl}
                />
                <S.Input
                  onChange={e => updateOrder(e, index)}
                  placeholder="조리 순서를 입력해주세요."
                  value={item.cookingOrder}
                  $width={100}
                  $marginLf={1}
                />
              </S.WrapOrder>
              {Boolean(index) && (
                <S.DeleteItem onClick={() => deleteItem(index, 'detail')}>
                  삭제
                </S.DeleteItem>
              )}
            </S.FlexWrap>
          ))}

          <S.AddButton onClick={() => addItem('order')} $width={50}>
            <IcAddRound size={1.2} color={palette.textSecondary} />
            조리 내용 추가
          </S.AddButton>

          <S.ErrorText>{errorValue.recipeDetailDtoList}</S.ErrorText>
        </S.WrapForm>

        <S.RequestButton onClick={checkForm}>
          {isEdit ? '수정하기' : '레시피 등록하기'}
        </S.RequestButton>
      </S.Container>
      {isCompleteModal && (
        <ModalWithoutButton
          content={modalContent}
          setIsModalFalse={() => setIsCompleteModal(false)}
        />
      )}

      <BottomNavigation />
    </>
  )
}

export default WriteRecipePage
